package com.xmingl.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;

@RestController
@RequestMapping("/system")
public class SystemController {

    @RequestMapping("getClassInfo")
    String getClassInfo(String clazzPath) throws ClassNotFoundException {
        Class<?> clazz = Class.forName(clazzPath);
        Field[] fields = clazz.getFields();
        JSONArray a = new JSONArray();
        for (Field f : fields) {
            a.add(f);
        }
        return a.toJSONString();
    }

}
