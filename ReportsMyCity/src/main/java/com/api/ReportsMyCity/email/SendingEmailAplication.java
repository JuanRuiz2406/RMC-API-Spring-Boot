package com.api.ReportsMyCity.email;

import com.api.ReportsMyCity.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class SendingEmailAplication {
    private JavaMailSender javaMailSender;
    private TemplateEngine templateEngine;
    @Autowired
    public void SendingEmailAplication(JavaMailSender javaMailSender,TemplateEngine templateEngine){
        this.templateEngine = templateEngine;
        this.javaMailSender = javaMailSender;

    }
    public String sendNotification(User user) throws MessagingException {
        Context context = new Context();
        context.setVariable("user",user);
        String process = templateEngine.process("verificationCode", context);
        javax.mail.internet.MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setSubject("Código de Seguridad de ReportsMyCity");
        helper.setText(process, true);
        helper.setTo(user.getEmail());
        javaMailSender.send(mimeMessage);
        return "Sent";

    }
}
