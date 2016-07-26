package com.lyubinbin.service;

import com.alibaba.fastjson.JSONObject;
import com.lyubinbin.util.ToutiaoUtil;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Lyu binbin on 2016/7/14.
 */
@Service
public class QiniuService {
    private static final Logger logger = LoggerFactory.getLogger(QiniuService.class);

    //设置好账号的ACCESS_KEY和SECRET_KEY
    String ACCESS_KEY = "1CDISPAnWSSMYEVsMxiv6DpEzZwlHWi_3ug6p2D9";
    String SECRET_KEY = "VbiSEH8ZoPxhvVt0-MOfrq5w3-TGvpcZIX5l67Pi";
    //要上传的空间
    String bucketname = "public";
    //上传到七牛后保存的文件名
    //String key = "my-java.png";
    //上传文件的路径
    //String FilePath = "C:/Users/Lyu binbin/Desktop/pic.png";

    //密钥配置
    Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
    //创建上传对象
    UploadManager uploadManager = new UploadManager();

    //简单上传，使用默认策略，只需要设置上传的空间名就可以了
    public String getUpToken(){
        return auth.uploadToken(bucketname);
    }

    // save image in Qiniu, not local
    public String saveImage(MultipartFile file) throws IOException {
        try {
            int dotPos = file.getOriginalFilename().lastIndexOf(".");
            if(dotPos < 0){
                return null;
            }
            String fileExt = file.getOriginalFilename().substring(dotPos + 1).toLowerCase();
            if(!ToutiaoUtil.isFileUploadAlloewed(fileExt)){
                return null;
            }
            // pic filed valid, save in local place, give it a random name
            String fileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + fileExt;
            //调用put方法上传
            Response response = uploadManager.put(file.getBytes(), fileName, getUpToken());
            //打印返回的信息
            //System.out.println(response.toString());
            if(response.isOK() && response.isJson()){
                return ToutiaoUtil.QINIU_DOMAIN_PREFIX + JSONObject.parseObject(response.bodyString()).get("key").toString();
            }
            else{
                logger.error("Qiniu upload error:" + response.bodyString());
                return null;
            }
        } catch (QiniuException e) {
            logger.error("Qiniu Service error:" + e.getMessage());
            return null;
        }
    }
}
