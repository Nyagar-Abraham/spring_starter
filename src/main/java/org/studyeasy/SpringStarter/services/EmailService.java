package org.studyeasy.SpringStarter.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.studyeasy.SpringStarter.utils.email.EmailDetails;

@Service
public class EmailService {
  @Autowired
  private JavaMailSender javaMailSender;

  @Value("${spring.mail.username}")
  private String sender;

  public boolean sendSimplemail(EmailDetails emailDetails){
     try {
      SimpleMailMessage mailMessage = new SimpleMailMessage();
      
      mailMessage.setFrom(sender);
      mailMessage.setTo(emailDetails.getRecipient());
      mailMessage.setSubject(emailDetails.getSubject());
      mailMessage.setText(emailDetails.getMsgBody());

      javaMailSender.send(mailMessage);

      return true;
     } catch (Exception e) {
       return false;
     }
  }
}
