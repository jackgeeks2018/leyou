package com.wpp.web;

import com.wpp.utils.AliyunStorage;
import com.wpp.utils.CharUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("upload")
public class UploadController {
    @Autowired
    private AliyunStorage storageService;
    /**
     * 上传图片功能
     * @param file
     * @return
     */
    @PostMapping("image")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        String key = generateKey(file.getOriginalFilename());
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        storageService.store(file, key);
        String url = storageService.generateUrl(key);
        if (StringUtils.isBlank(url)) {
            // url为空，证明上传失败
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        // 返回200，并且携带url路径
        return ResponseEntity.ok(url);
    }


    private String generateKey(String originalFilename){
        int index = originalFilename.lastIndexOf('.');
        String suffix = originalFilename.substring(index);

        String key = null;
        key = CharUtil.getRandomString(20) + suffix+System.currentTimeMillis()+"";

        return key;
    }
}
