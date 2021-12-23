package com.chan.labelimg.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author: chen zheng
 * @Date: 12/23/2021 9:37 PM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageLabel {
    int imageID;
    List<Point> points;
    List<String> remark;
    int userID;
}
