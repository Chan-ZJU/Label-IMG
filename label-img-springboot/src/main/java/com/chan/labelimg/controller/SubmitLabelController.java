package com.chan.labelimg.controller;

import com.chan.labelimg.pojo.ImageLabel;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author: chen zheng
 * @Date: 12/23/2021 9:40 PM
 */
@RestController
@RequestMapping("/api")
public class SubmitLabelController {
    @CrossOrigin
    @PostMapping("submitLabel")
    public int submitLabelResult(@RequestBody ImageLabel imageLabel) {
        System.out.println(imageLabel.getImageID());
        System.out.println(imageLabel.getPoints());
        System.out.println(imageLabel.getRemark());
        return 1;
    }
}
