package com.chan.labelimg;

import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LabelImgSpringbootApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void fastJsonTest() {
        String point = "[Point(x=378, y=102), Point(x=462, y=102), Point(x=462, y=156), Point(x=378, y=156), Point(x=582, y=118), Point(x=658, y=118), Point(x=658, y=204), Point(x=582, y=204)]";
        JSONObject jsonObject = JSONObject.parseObject(point);
        System.out.println(jsonObject);
    }

}
