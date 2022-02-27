package com.xuyang.OnlyApp.controller;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/file")
@Slf4j
public class ProcessFile {

    private final String URL_PREFIX;
    private final UploadManager uploadManager;
    private final String upToken;

    @Autowired
    public ProcessFile(Environment env) {
        URL_PREFIX =env.getProperty("qiniu.URL_PREFIX");

        Configuration cfg = new Configuration(Region.huanan());
        uploadManager = new UploadManager(cfg);

        Auth auth = Auth.create(env.getProperty("qiniu.AccessKey"), env.getProperty("qiniu.SecretKey"));
        upToken = auth.uploadToken(env.getProperty("qiniu.Bucket"));
    }

    @PostMapping("/upload")
    Map<String, Object> receiveFile(@RequestParam("file[]") MultipartFile[] files) {
        Map<String, Object> result = new HashMap<>();
        ArrayList<String> errFiles = new ArrayList<>();
        HashMap<String, String> succMap = new HashMap<>();

        for (MultipartFile file : files) {
            try {
                Response response = uploadManager.put(file.getInputStream(), null, upToken, null, null);
                String file_key = response.jsonToMap().get("key").toString();
                String file_path = URL_PREFIX + file_key;
                succMap.put(file.getOriginalFilename(), file_path);
                log.info("已上传：" + file_path);
            } catch (Exception e) {
                System.err.println(e.getMessage());
                errFiles.add(file.getOriginalFilename());
                log.warn(file.getOriginalFilename() + " 上传失败");
            }
        }

        result.put("msg", succMap.size() + "个文件上传成功，" + errFiles.size() + "个文件上传失败");
        result.put("code", 200);
        HashMap<String, Object> data = new HashMap<>();
        data.put("errFiles", errFiles);
        data.put("succMap", succMap);
        result.put("data", data);

        return result;
    }

}
