package com.chan.labelimg.service;

import com.chan.labelimg.mapper.ImgMapper;
import com.chan.labelimg.pojo.Img;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: chen zheng
 * @Date: 12/5/2021 3:02 PM
 */
@Service
public class ImageService {
    @Autowired
    private ImgMapper imgMapper;

    public int uploadImg(int fromID, String url) {
        return imgMapper.uploadImg(fromID, url);
    }

    public List<Img> getMyImg(int fromID) {
        return imgMapper.getImg(fromID);
    }
}
