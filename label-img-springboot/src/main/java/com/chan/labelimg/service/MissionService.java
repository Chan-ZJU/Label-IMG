package com.chan.labelimg.service;

import com.chan.labelimg.mapper.MissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: chen zheng
 * @Date: 12/9/2021 5:23 PM
 */
@Service
public class MissionService {
    @Autowired
    private MissionMapper missionMapper;

    public int createMission(String desc, int fromID) {
        return missionMapper.insertMission(desc, fromID);
    }
}
