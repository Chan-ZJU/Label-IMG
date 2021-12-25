package com.chan.labelimg.service;

import com.chan.labelimg.mapper.MissionMapper;
import com.chan.labelimg.pojo.ImageMission;
import com.chan.labelimg.pojo.Img;
import com.chan.labelimg.pojo.Mission;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        for (int imageID : selectedImages) {
            missionMapper.updateImages(missionID, imageID);
        }
        return missionID;
    }

    public List<Mission> getMyMission(int fromID) {
        return missionMapper.getMyMission(fromID);
    }

    public Mission getMissionByMissionID(int ID) {
        return missionMapper.getMissionByMissionID(ID);
    }

    public List<Mission> getAllMissions() {
        return missionMapper.getAllMission();
    }

    public List<Img> getImgByMissionID(int id) {
        List<Integer> ID = missionMapper.getImgIDByMissionID(id);
        return missionMapper.getImgByMissionID(ID);
    }

    public int claimMission(int userID, int missionID) {
        return missionMapper.claimMission(missionID, userID);
    }

    public List<Mission> getMyClaimedMission(int toID) {
        return missionMapper.getClaimedMission(toID);
    }

    public int submitMission(int ID) {
        return missionMapper.submitMission(ID);
    }

    public List<Mission> getCheck() {
        return missionMapper.getCheck();
    }

    public ImageMission getSingleImg(int imgID, int missionID) {
        return missionMapper.getSingleImg(imgID, missionID);
    }

    public int finishMission(int missionID) {
        return missionMapper.finishMission(missionID);
    }

    public List<Integer> getCount() {
        List<Integer> countList = new ArrayList<>();
        countList.add(missionMapper.getAllMissionCount());
        countList.add(missionMapper.getNewMissionCount());
        countList.add(missionMapper.getClaimedMissionCount());
        countList.add(missionMapper.getFinishedMissionCount());
        return countList;
    }
}
