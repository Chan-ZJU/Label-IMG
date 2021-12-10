package com.chan.labelimg.controller;

import com.chan.labelimg.pojo.Mission;
import com.chan.labelimg.pojo.Result;
import com.chan.labelimg.service.MissionService;
import io.swagger.annotations.Api;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

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

    @CrossOrigin
    @PostMapping("createMission")
    public Result createMission(@RequestParam("missionDesc") String desc, @RequestParam("fromID") int fromID, @RequestParam("choose") String choose) {
        String[] split = choose.split(",");
        System.out.println(Arrays.toString(split));
        List<Integer> selectedImages = new ArrayList<>();
        for (int i = 0; i < split.length; i++) {
            if (!split[i].isEmpty()) {
                selectedImages.add(i);
            }
        }
        missionService.createMission(desc, fromID, selectedImages);
        return new Result(200, -1);
    }

    @CrossOrigin
    @PostMapping("myMission")
    public List<Mission> getMyMission(@RequestParam("userID") int fromID) {
        return missionService.getMyMission(fromID);
    }

    @CrossOrigin
    @PostMapping("missions")
    public List<Mission> getAllMissions() {
        return missionService.getAllMissions();
    }
}
