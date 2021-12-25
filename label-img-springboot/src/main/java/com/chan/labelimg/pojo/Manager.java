package com.chan.labelimg.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: chen zheng
 * @Date: 12/25/2021 5:41 PM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Manager {
    int id;
    String username;
    String password;
}
