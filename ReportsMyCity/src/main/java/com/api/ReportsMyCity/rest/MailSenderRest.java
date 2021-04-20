package com.api.ReportsMyCity.rest;

import com.api.ReportsMyCity.email.RandomString;
import com.api.ReportsMyCity.email.SendingEmailAplication;
import com.api.ReportsMyCity.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;

@RestController
public class MailSenderRest {

    @Autowired
    private SendingEmailAplication sendingEmailAplication;

    @RequestMapping("/sendEmail")
    public String Send() throws MessagingException {
        RandomString random = new RandomString();
        User user =  new User();
        user.setName("Marco");
        user.setLastname(random.nextString());
        user.setEmail("alvaradomarco2011@gmail.com");
        sendingEmailAplication.sendNotification(user);
        return "enviado";
    }


}
