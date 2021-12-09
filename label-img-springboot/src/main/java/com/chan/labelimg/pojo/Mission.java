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
     * 0 -- just created
     * 1 -- is claimed
     * 2 -- is under-checking
     * 3 -- is finished
     */
    int state;
}
