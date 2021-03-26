package com.api.ReportsMyCity.email;

import com.api.ReportsMyCity.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;
@Service
public class SendingEmailAplication {
    private JavaMailSender javaMailSender;

    @Autowired
    public void SendingEmailAplication(JavaMailSender javaMailSender){
        this.javaMailSender = javaMailSender;
    }
    public void sendNotification(User user) throws MailException {

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(user.getEmail());
        mail.setFrom("reportsmycity@gmail.com");
        mail.setSubject("Restablece tu contraseña");
        mail.setText("Hola " + user.getName() + " " + user.getLastname() + " Hemos sido notificados " +
                "que deseas restablecer tu contraseña, para esto debes de seguir los siguientes pasos: ");
        javaMailSender.send(mail);
    }
}
