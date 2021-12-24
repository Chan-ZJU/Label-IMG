package com.chan.labelimg.service;

import com.chan.labelimg.mapper.LabelMissionMapper;
import com.chan.labelimg.pojo.ImageMission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: chen zheng
 * @Date: 12/24/2021 4:17 PM
 */
@Service
public class LabelMissionService {
    @Autowired
    private LabelMissionMapper labelMissionMapper;

    public int updateLabelResult(String pointsX, String pointsY, String remarks, int sizeX, int sizeY, int missionID, int imageID) {
        return labelMissionMapper.updateImageMission(pointsX, pointsY, remarks, sizeX, sizeY, missionID, imageID);
    }

    public List<ImageMission> getImageMissionByMissionID(int id){
        return labelMissionMapper.getImageMissionByMissionID(id);
    }
}
