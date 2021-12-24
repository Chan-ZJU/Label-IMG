package com.chan.labelimg.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: chen zheng
 * @Date: 12/24/2021 5:03 PM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageMission {
    int imageID;
    int missionID;
    String pointsX;
    String pointsY;
    String remarks;
    int sizeX;
    int sizeY;
}
