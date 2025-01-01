package com.example.usermicroservice.services;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.usermicroservice.entities.UserState;
import com.example.commonresources.events.BikeRentEvent;
import com.example.commonresources.events.CreateUserEvent;
import com.example.usermicroservice.dtos.LoginRequest;
import com.example.usermicroservice.dtos.UserDto;
import com.example.usermicroservice.dtos.UserSecurity;
import com.example.usermicroservice.entities.User;
import com.example.usermicroservice.exceptions.EmailNotValidException;
import com.example.usermicroservice.exceptions.MyBadRequestException;
import com.example.usermicroservice.exceptions.PasswordNotCorrectException;
import com.example.usermicroservice.exceptions.UserEmailException;
import com.example.usermicroservice.exceptions.UserNotFoundException;
import com.example.usermicroservice.exceptions.UserUsernameException;
import com.example.usermicroservice.repositories.UserRepository;

@Service
public class UserService {
    private final Integer priceRentHour=5;//the rental price for one full hour
    @Autowired
    private UserRepository userRepository;
    @Autowired
    @Qualifier("kafkaTemplateCreateUserEvent")
    private KafkaTemplate<String, CreateUserEvent> kafkaTemplateCreateUserEvent;
    @Autowired
    @Qualifier("kafkaTemplateForBikeRentEvent")
    private KafkaTemplate<String, BikeRentEvent> kafkaTemplateForBikeRentEvent;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void createUser(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new UserEmailException();
        } else if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new UserUsernameException();
        } else {
            userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
            userRepository.save(new User(userDto));
            // verification of email
            kafkaTemplateCreateUserEvent.send("topic-createuser",
                    new CreateUserEvent(userDto.getEmail(), userDto.getName()));
        }
    }

    @KafkaListener(topics = "topic-correcttoken", containerFactory = "kafkaListenerContainerFactoryString")
    public void changeState(String email) {
        // in real it will be always(maybe) true because of logic of programm
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserEmailException());
        user.setUserState(UserState.VERIFICATED);
        userRepository.save(user);
    }

    @Scheduled(fixedDelay = 10000)
    @Transactional
    public void clearNotVerificatedUser() {
        String sql = """
                DELETE FROM user
                WHERE user_state = 'INPROCESS'
                  AND TIMESTAMPDIFF(MINUTE, registration_date, NOW()) > 5;
                """;
    
            jdbcTemplate.update(sql);
        
    }
    
    public void checkIsEmailValid(String email) {
        if (!userRepository.existsByEmail(email)) {
            throw new EmailNotValidException();
        }
    }

    public void checkCredentials(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new PasswordNotCorrectException());
        System.out.println(user);
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new PasswordNotCorrectException();
        }
    }
    // part of saga transaction rent

    @KafkaListener(topics = "successBikeChangedStatusTopic", containerFactory = "kafkaListenerContainerFactoryBikeRentEvent")
    public void checkUserBalance(BikeRentEvent bikeRentEvent) {
        try {
            User user = userRepository.findById(bikeRentEvent.getIdUser())
                    .orElseThrow(() -> new UserNotFoundException());
            Integer price =bikeRentEvent.getTimeRentHours()*priceRentHour;
            if (!user.getIsInTransaction() && user.getBalance() >= price) {
                user.setIsInTransaction(true);
                user.setBalance(user.getBalance() - price);
                userRepository.save(user);
                kafkaTemplateForBikeRentEvent.send("successCheckUserBalanceTopic", bikeRentEvent);
            } else {
                throw new UserNotFoundException();// we should create another exception but it works
            }
        } catch (UserNotFoundException e) {
<<<<<<< HEAD
            Integer price =bikeRentEvent.getTimeRentHours()*priceRentHour;
            User user = userRepository.findById(bikeRentEvent.getIdUser()).get();
            user.setIsInTransaction(false);
            user.setBalance(user.getBalance() + price);
=======
>>>>>>> fad0f612ede9d27af9bf3601e55c1e16a4745d52
            kafkaTemplateForBikeRentEvent.send("rollbackCheckUserBalanceTopic", bikeRentEvent);
        }
    }

    @KafkaListener(topics = "successRentBikeTopic", containerFactory = "kafkaListenerContainerFactoryBikeRentEvent")
    public void successRentBikeTopic(BikeRentEvent bikeRentEvent) {
        User user = userRepository.findById(bikeRentEvent.getIdUser()).get();
        user.setIsInTransaction(false);
        userRepository.save(user);
    }

    public UserSecurity getUserSecurityByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new MyBadRequestException());
        UserSecurity userSecurity = new UserSecurity();
        userSecurity.setIdUser(user.getId());
        userSecurity.setUsername(username);
        return userSecurity;
    }
}