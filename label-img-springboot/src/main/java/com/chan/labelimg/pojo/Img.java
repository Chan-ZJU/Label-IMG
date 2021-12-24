package com.chan.labelimg.pojo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: chen zheng
 * @Date: 12/5/2021 2:26 PM
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "图片管理")
public class Img {
    int id;
    int fromID;
    String url;
}
