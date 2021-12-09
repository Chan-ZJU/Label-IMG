package com.chan.labelimg.controller;

import com.chan.labelimg.service.MissionService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author: chen zheng
 * @Date: 12/9/2021 5:25 PM
 */
@Api(tags = "任务管理")
@RestController
@RequestMapping(value = "api")
public class MissionController {
    @Resource
    private MissionService missionService;

    public int createMission(String desc, int fromID){

    }
}
