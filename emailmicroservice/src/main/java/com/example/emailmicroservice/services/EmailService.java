package com.example.emailmicroservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.example.commonresources.events.CreateUserEvent;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

  @Autowired
  private JavaMailSender mailSender;
  @Value("${name.securemicroservice.getverificationtoken}")
  private String urlGetverificationtoken;
  @Value("${name.securemicroservice.activateaccount}")
  private String urlDoVerificationAccount;
  @Autowired
  private TemplateEngine templateEngine;
  @Autowired
  private RestTemplate restTemplate;

  @KafkaListener(topics = "topic-createuser",containerFactory = "kafkaListenerContainerFactoryCreateUserEvent")
  public void sendHtmlEmail(CreateUserEvent createUserEvent) throws MessagingException {
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

    Context context = new Context();
    Map<String, Object> variables = new HashMap<>();
    variables.put("name", createUserEvent.getName());
    // we have to take jwt from securemicroservice
    ResponseEntity<String> token = restTemplate.getForEntity(urlGetverificationtoken+"?email="+createUserEvent.getEmail(), String.class);
    variables.put("link",urlDoVerificationAccount+token.getBody());
    context.setVariables(variables);

    String htmlContent = templateEngine.process("email-template", context);

    helper.setTo(createUserEvent.getEmail());
    helper.setSubject("Verification");
    helper.setText(htmlContent, true);
    helper.setFrom("");
    mailSender.send(message);
  }
}
