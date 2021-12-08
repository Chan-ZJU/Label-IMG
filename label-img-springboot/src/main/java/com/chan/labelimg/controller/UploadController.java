package com.chan.labelimg.controller;

import com.chan.labelimg.pojo.Result;
import com.chan.labelimg.service.UploadService;
import com.chan.labelimg.utils.OssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * @author: chen zheng
 * @Date: 12/5/2021 3:01 PM
 */
@Api(tags = "上传管理")
@RestController
@RequestMapping("/api")
public class UploadController {
    @Resource
    private UploadService uploadService;

    @Value("${file.upload.path}")
    private String filePath;

    @Autowired
    private OssService ossService;

    @CrossOrigin
    @PostMapping("/ossUpload")
    public String uploadOssFile(@RequestParam("file") MultipartFile file, @RequestParam("ID") int ID, @RequestParam("type") int type) {
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String url = ossService.uploadFile(inputStream, file.getOriginalFilename());
        if (type == 0) {
            uploadService.uploadImg(ID, url);
        } else {
            uploadService.uploadVideo(ID, url);
        }
        return url;
    }

    @CrossOrigin
    @PostMapping("/upload")
    @ApiOperation(value = "上传文件")
    public String upload(@RequestParam(value = "file") MultipartFile multipartFiles, @RequestParam("ID") int ID) throws IOException {
        File dir = new File(filePath);
        System.out.println(ID);
        System.out.println(dir.toString());
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String suffix = multipartFiles.getOriginalFilename().substring(multipartFiles.getOriginalFilename().lastIndexOf("."));
        String newFileName = UUID.randomUUID().toString().replaceAll("-", "") + suffix;
        File newFile = new File(filePath + newFileName);
        System.out.println(newFile.toString());
        try {
            multipartFiles.transferTo(newFile);
            uploadService.uploadVideo(ID, newFileName);
            return newFileName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
