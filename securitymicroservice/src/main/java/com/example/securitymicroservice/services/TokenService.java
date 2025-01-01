package com.example.securitymicroservice.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.securitymicroservice.dtos.LoginRequest;
import com.example.securitymicroservice.dtos.MainTokens;
import com.example.securitymicroservice.dtos.ResponseCheck;
import com.example.securitymicroservice.entities.Token;
import com.example.securitymicroservice.exceptions.EmailNotCorrectException;
import com.example.securitymicroservice.exceptions.ExceptionTokenNotFound;
import com.example.securitymicroservice.exceptions.MyBadRequestException;
import com.example.securitymicroservice.repositories.TokenRepository;
import com.example.securitymicroservice.utils.JwtUtils;

@Service
public class TokenService {
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${name.usermicroservice.urlCheckValidToken}")
    private String urlCheckValidToken;
    @Value("${name.usermicroservice.urlCheckCredentials}")
    private String urlCheckCredentials;

    public void addToken(Token token) {
        tokenRepository.save(token);
    }

    public void doVerificationAccount(String token) {
        Token token2 = tokenRepository.findByToken(token);
        if (token2 != null) {
            tokenRepository.deleteById(token2.getId());// now we can delete token
            tokenRepository.flush();
            if(!jwtUtils.validateVerificationToken(token))
            {
            throw new ExceptionTokenNotFound(token);
            }
            // send topic to usermicroservice for change the state of account
            kafkaTemplate.send("topic-correcttoken", jwtUtils.extractEmailOfVerificationToken(token));
        } else {
            throw new ExceptionTokenNotFound(token);
        }
    }

    public String getVerificationToken(String email) {
        String token = jwtUtils.generateVerificationToken(email);
        Token token2 = new Token();
        token2.setToken(token);
        addToken(token2);
        return token;
    }
    private void deleteVerificationToken(String verificationToken)
    {
        tokenRepository.deleteByToken(verificationToken);
        tokenRepository.flush();
    }
    private boolean isJwtFormat(String token) {
        return token != null && token.split("\\.").length == 3;
    }
    @Scheduled(fixedDelay = 10000)
@Transactional
public void clearTokensNotActual() {
    List<Token> tokens = tokenRepository.findAll();

    for (Token token : tokens) {
        String tokenString = token.getToken();

        // Перевіряємо, чи це взагалі JWT
        if (!isJwtFormat(tokenString)) {
            deleteVerificationToken(tokenString);
            continue; // Пропускаємо далі
        }

        // Перевіряємо валідність токена
        if (!jwtUtils.validateVerificationToken(tokenString)) {
            deleteVerificationToken(tokenString);
            continue; // Пропускаємо далі
        }

        // Перевіряємо токен через зовнішній сервіс
        try {
            restTemplate.getForObject(
                urlCheckValidToken + "?email=" + jwtUtils.extractEmailOfVerificationToken(tokenString),
                Boolean.class
            );
        } catch (HttpClientErrorException e) {
            deleteVerificationToken(tokenString);
        }
    }
}

    public MainTokens login(LoginRequest loginRequest) {
        // Установка заголовків
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Створення об'єкта запиту
        HttpEntity<LoginRequest> request = new HttpEntity<>(loginRequest, headers);
        try {
            restTemplate.postForObject(urlCheckCredentials, request, Boolean.class);
                return new MainTokens(jwtUtils.generateRefreshToken(loginRequest.getUsername()),
                jwtUtils.generateAccessToken(loginRequest.getUsername()));
        } catch (HttpClientErrorException e) {
                MainTokens mainTokens = new MainTokens();
                mainTokens.setSuccess(false);
                return mainTokens;
        }
    }

    public String getUsernameByAccessToken(String accessToken) {
        if(!jwtUtils.validateAccessToken(accessToken))
        {
        throw new MyBadRequestException();
        }
        return jwtUtils.extractUsernameOfAccessToken(accessToken);
    }
}
