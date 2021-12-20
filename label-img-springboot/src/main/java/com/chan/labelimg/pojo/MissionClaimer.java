package com.chan.labelimg.pojo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: chen zheng
 * @Date: 12/20/2021 12:03 PM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "任务领取")
public class MissionClaimer {
    int fromID;
    int missionID;
}
