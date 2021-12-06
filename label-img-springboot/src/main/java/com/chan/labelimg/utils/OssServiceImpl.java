package com.chan.labelimg.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.UUID;

/**
 * @author: chen zheng
 * @Date: 12/6/2021 9:36 PM
 */
@Service
public class OssServiceImpl implements OssService {
    @Override
    public String uploadFile(InputStream inputStream, String originalFilename) {
        String endPoint = OssPropertiesUtils.END_POINT;
        String accessKeyID = OssPropertiesUtils.KEY_ID;
        String accessKeySecret = OssPropertiesUtils.KEY_SECRET;
        String bucketName = OssPropertiesUtils.BUCKET_NAME;

        OSS ossClient = new OSSClientBuilder().build(endPoint, accessKeyID, accessKeySecret);

        String folder = new DateTime().toString("yyyy/MM/dd");
        String fileName = UUID.randomUUID().toString();
        String fileSuffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String objectName = folder + "/" + fileName + fileSuffix;

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType("image/jpg");

        ossClient.putObject(bucketName, objectName, inputStream, objectMetadata);

        ossClient.shutdown();
        return "http://" + bucketName + "." + endPoint + "/" + objectName;
    }
}
