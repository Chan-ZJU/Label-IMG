package com.chan.labelimg.controller;

import com.chan.labelimg.pojo.FromID;
import com.chan.labelimg.pojo.Img;
import com.chan.labelimg.service.ImageService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: chen zheng
 * @Date: 12/7/2021 3:41 PM
 */
@Api(tags = "图片管理")
@RestController
@RequestMapping("/api")
public class ImgController {
    @Resource
    private ImageService imageService;

    @CrossOrigin
    @PostMapping("/myImg")
    public List<Img> myImg(@RequestBody FromID fromID) {
        return imageService.getMyImg(fromID.getFromID());
    }
}
