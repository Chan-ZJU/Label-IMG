package com.chan.labelimg.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: chen zheng
 * @Date: 12/28/2021 9:02 PM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MissionDesc {
    int id;
    String description;
    int fromID;
    int toID;
    /**
     * state:
     * 0 -- just created (for user to claim)
     * 1 -- is claimed (not finish)
     * 2 -- is finished (for manager to check)
     * 3 -- done
     */
    int state;
    String user;
}
