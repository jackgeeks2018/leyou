package com.wpp.utils;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Date;
import java.util.stream.Stream;

/**
 * @author Yogeek
 * @date 2018/7/16 16:10
 * @decrpt 阿里云对象存储服务
 */
@Service
public class AliyunStorage implements Storage {
    @Value("${aliyun.endpoint}")
    private  String endpoint;
    @Value("${aliyun.accessKeyId}")
    private  String accessKeyId;
    @Value("${aliyun.accessKeySecret}")
    private  String accessKeySecret;
    @Value("${aliyun.bucketName}")
    private  String bucketName;

    /**
     * 获取阿里云OSS客户端对象
     *
     * @return ossClient
     */
    private OSSClient getOSSClient(){
        return new OSSClient(endpoint,accessKeyId, accessKeySecret);
    }

    private String getBaseUrl() {
        return "https://" + bucketName + "." +  endpoint + "/" ;
    }

    /**
     * 阿里云OSS对象存储简单上传实现
     */
    @Override
    public void store(MultipartFile file, String keyName) {
        try {
            // 简单文件上传, 最大支持 5 GB, 适用于小文件上传, 建议 20M以下的文件使用该接口
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());
            // 对象键（Key）是对象在存储桶中的唯一标识。
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, keyName, file.getInputStream(), objectMetadata);
            PutObjectResult putObjectResult = getOSSClient().putObject(putObjectRequest);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public Stream<Path> loadAll() {
        return null;
    }

    @Override
    public Path load(String keyName) {
        return null;
    }

    @Override
    public Resource loadAsResource(String keyName) {
        try {
            URL url = new URL(getBaseUrl() + keyName);
            Resource resource = new UrlResource(url);
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                return null;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void delete(String keyName) {
        try {
            getOSSClient().deleteObject(bucketName, keyName);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public String generateUrl(String keyName) {

        // 设置URL过期时间为10年 3600l* 1000*24*365*10
        Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 10);
        // 生成URL
        URL url =  this.getOSSClient().generatePresignedUrl(bucketName, keyName, expiration);
        if (url != null) {
            return url.toString();
        }
        return null;
    }


}
