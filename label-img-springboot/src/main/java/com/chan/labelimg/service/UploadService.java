package com.chan.labelimg.service;

import com.chan.labelimg.mapper.ImgMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: chen zheng
 * @Date: 12/5/2021 3:02 PM
 */
@Service
public class UploadService {
    @Autowired
    private ImgMapper imgMapper;

    public int uploadImg(int fromID, String url) {
        return imgMapper.uploadImg(fromID, url);
    }
}
