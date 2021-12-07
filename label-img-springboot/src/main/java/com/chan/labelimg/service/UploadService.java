package com.chan.labelimg.service;

import com.chan.labelimg.mapper.UploadMapper;
import com.chan.labelimg.pojo.Img;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: chen zheng
 * @Date: 12/5/2021 3:02 PM
 */
@Service
public class UploadService {
    @Autowired
    private UploadMapper uploadMapper;

    public int uploadImg(int fromID, String url) {
        return uploadMapper.uploadImg(fromID, url);
    }

    public int uploadVideo(int fromID, String url) {
        return uploadMapper.uploadVideo(fromID, url);
    }

    public List<Img> getMyImg(int fromID) {
        return uploadMapper.getImg(fromID);
    }
}
