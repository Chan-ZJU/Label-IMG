package com.chan.labelimg.pojo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * @author: chen zheng
 * @Date: 12/4/2021 2:39 PM
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "返回码")
public class Result {
    private int code;
}
