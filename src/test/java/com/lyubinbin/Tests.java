package com.lyubinbin;

import com.lyubinbin.util.ToutiaoUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * test for password
 * Created by Lyu binbin on 2016/7/26.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ToutiaoNewsApplication.class)
public class Tests {
    @Test
    public void psw(){
        System.out.println(ToutiaoUtil.MD5("123456" + "1234567890"));
    }
}
