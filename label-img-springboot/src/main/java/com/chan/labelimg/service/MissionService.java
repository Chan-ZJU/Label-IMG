package com.chan.labelimg.service;

import com.chan.labelimg.mapper.MissionMapper;
import com.chan.labelimg.mapper.UploadMapper;
import com.chan.labelimg.pojo.Mission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @author: chen zheng
 * @Date: 12/9/2021 5:23 PM
 */
@Service
public class MissionService {
    @Autowired
    private MissionMapper missionMapper;

    public int createMission(String desc, int fromID, List<Integer> selectedImages) {
        missionMapper.insertMission(desc, fromID);
        int missionID = missionMapper.getMissionID();
        missionMapper.updateImages(missionID, selectedImages);
        return missionID;
    }

    public List<Mission> getMyMission(int fromID) {
        return missionMapper.getMyMission(fromID);
    }

    public List<Mission> getAllMissions(){
        return missionMapper.getAllMission();
    }
}
