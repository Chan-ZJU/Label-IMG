package com.chan.labelimg.utils;

import java.io.InputStream;

/**
 * @author: chen zheng
 * @Date: 12/6/2021 9:35 PM
 */
public interface OssService {
    String uploadFile(InputStream inputStream, String originalFilename);
}
