package com.chan.labelimg.controller;

import com.chan.labelimg.pojo.*;
import com.chan.labelimg.service.MissionService;
import io.swagger.annotations.Api;
import io.swagger.models.auth.In;
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
    @PostMapping("getSingleMission")
    public Mission getMissionByMissionID(@RequestBody FromID fromID) {
        return missionService.getMissionByMissionID(fromID.getFromID());
    }

    @CrossOrigin
    @PostMapping("missions")
    public List<Mission> getAllMissions() {
        return missionService.getAllMissions();
    }

    @CrossOrigin
    @PostMapping("missionDesc")
    public List<Img> getImgByMissionID(@RequestBody FromID ID) {
        return missionService.getImgByMissionID(ID.getFromID());
    }

    @CrossOrigin
    @PostMapping("claimMission")
    public int claimMission(@RequestBody MissionClaimer missionClaimer) {
        return missionService.claimMission(missionClaimer.getFromID(), missionClaimer.getMissionID());
    }

    @CrossOrigin
    @PostMapping("myClaimedMission")
    public List<Mission> getMyClaimedMission(@RequestBody FromID fromID) {
        return missionService.getMyClaimedMission(fromID.getFromID());
    }
}
