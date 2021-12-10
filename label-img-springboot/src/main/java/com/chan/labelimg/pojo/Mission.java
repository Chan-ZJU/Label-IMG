package com.chan.labelimg.pojo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: chen zheng
 * @Date: 12/9/2021 5:14 PM
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "任务管理")
public class Mission {
    int id;
    String description;
    int fromID;
    int toID;
    /**
     * state:
     * 0 -- just created (for user to claim)
     * 1 -- is claimed (for manager to check)
     * 2 -- is finished
     */
    int state;
}
