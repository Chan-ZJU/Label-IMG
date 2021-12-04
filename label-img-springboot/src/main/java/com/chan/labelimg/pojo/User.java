package com.chan.labelimg.pojo;

import com.chan.labelimg.validate.Signup;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

/**
 * @author: chen zheng
 * @Date: 12/4/2021 2:32 PM
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "用户信息")
public class User {
    int id;

    @NotBlank
    @Size(min = 6, max = 50)
    String username;

    @NotBlank
    @Size(min = 6)
    String password;

    @Email(groups = Signup.class)
    @NotBlank(groups = Signup.class)
    String email;
}
