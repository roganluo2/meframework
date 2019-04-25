package com.gperedu.meframework.demo.service.impl;

import com.gperedu.meframework.annotation.GPService;
import com.gperedu.meframework.demo.service.IQueryService;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description TODO
 * @Date 2019/3/27 19:31
 * @Created by rogan.luo
 */
@GPService
public class QueryService implements IQueryService {


    @Override
    public String query(String name) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date());
        String json = "{name:\"" + name + "\",time\"" + time + "\"}";
        System.out.println("这是业务方法中答应的" + json);
        return json;
    }
}
