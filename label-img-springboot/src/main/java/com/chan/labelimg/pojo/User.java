package com.chan.labelimg.pojo;

import com.chan.labelimg.validate.Signup;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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

    @NotNull
    @Size(min = 6, max = 50)
    String username;

    @NotNull
    @Min(6)
    String password;

    @Email(groups = Signup.class)
    @NotNull(groups = Signup.class)
    String email;
}
