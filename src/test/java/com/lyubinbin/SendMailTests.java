package com.lyubinbin;

import com.lyubinbin.util.MailSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * test sending mails
 * Created by Lyu binbin on 2016/7/25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ToutiaoNewsApplication.class)
public class SendMailTests {
    @Autowired
    MailSender mailSender;

    @Test
    public void mailsenderTest(){
        Map<String, Object> map = new HashMap<>();
        map.put("username", "Binbin");
        mailSender.sendWithHTMLTemplate("cxlvbinbin@pku.edu.cn", "ToutiaoNews", "mails/welcome.html", map);
    }
}
