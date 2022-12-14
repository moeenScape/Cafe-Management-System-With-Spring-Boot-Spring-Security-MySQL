package com.moeen.Newcafe.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;

@Service
public class EmailUtils {
    @Autowired
    private JavaMailSender javaMailSender;
    public void sendSimpleMail(String to, String subject, String text, List<String> list)
    {
        SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
        simpleMailMessage.setFrom("mointest1224@gmail.com");
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(text);
        if(list!=null && list.size()>0)
            simpleMailMessage.setCc(getCcArray(list));
        javaMailSender.send(simpleMailMessage);

    }
    private String[] getCcArray(List<String > cCList){
        String[] cc=new String[cCList.size()];
        for (int i=0;i<cCList.size();i++)
        {
            cc[i]=cCList.get(i);
        }
        return cc;
    }
    public void forgetMail(String to,String subject,String password) throws MessagingException{
        MimeMessage message=javaMailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(message,true);
        helper.setFrom("mointest1224@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        String htmlMsg="<p><b>Your Login Details</b><br><b>Email:</b>" + to + "<br><b>Password:</b>"+password+"<br><a herf:\\\"http://localhost:4200\">Click here</a></p>";
        message.setContent(htmlMsg,"text/html");
        javaMailSender.send(message);


    }
}
