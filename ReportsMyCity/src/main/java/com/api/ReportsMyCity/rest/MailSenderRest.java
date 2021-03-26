package com.api.ReportsMyCity.rest;

import com.api.ReportsMyCity.email.SendingEmailAplication;
import com.api.ReportsMyCity.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MailSenderRest {

    @Autowired
    private SendingEmailAplication sendingEmailAplication;

    @RequestMapping("/sendEmail")
    public String Send(){
        User user =  new User();
        user.setName("Marco");
        user.setLastname("Alvarado");
        user.setEmail("alvaradomarco2011@gmail.com");
        try {
            sendingEmailAplication.sendNotification(user);
        }catch(MailException e){
            System.out.println(e.getMessage());
        }
        return "enviado";
    }
}
