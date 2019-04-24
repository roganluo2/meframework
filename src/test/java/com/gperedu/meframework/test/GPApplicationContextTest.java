package com.gperedu.meframework.test;

import com.gperedu.meframework.context.GPApplicationContext;
import com.gperedu.meframework.demo.controller.UserController;
import org.junit.Test;

/**
 * Created by rogan on 2019/4/20.
 */
public class GPApplicationContextTest {


    @Test
    public void testApplicationContext(){
        try {
            GPApplicationContext applicationContext = new GPApplicationContext("classpath:application.properties");
            System.out.println(applicationContext.getBean("userController"));
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }


}
