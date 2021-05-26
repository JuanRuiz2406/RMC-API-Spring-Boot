package com.api.ReportsMyCity.rest;

import com.api.ReportsMyCity.email.SendingEmailAplication;
import com.api.ReportsMyCity.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;

@RestController
public class MailSenderRest {

    @Autowired
    private SendingEmailAplication sendingEmailAplication;

    public MailSenderRest() {

    }

    @RequestMapping("/sendEmail")
    public String Send(User user) throws MessagingException {
        sendingEmailAplication.sendNotification(user);
        return "enviado";
    }


}
