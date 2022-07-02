package com.atguigu.guli.service.oss.serivce.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import com.atguigu.guli.service.oss.serivce.FileService;
import com.atguigu.guli.service.oss.util.OssProperties;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.UUID;

/**
 * @Description TODO
 * @Auther chengjiahui
 * @Date 2022/4/14 09:59
 * @Version 1.0
 */
@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private OssProperties ossProperties;

    @Override
    public String upload(InputStream inputStream, String module, String originalFileName) {
        String endpoint=ossProperties.getEndpoint();
        String keyid=ossProperties.getKeyid();
        String keysecret=ossProperties.getKeysecret();
        String bucketname= ossProperties.getBucketname();
        //判断oss实例是否存在，如果不存在则创建，如果存在则获取
        OSS ossClient=new OSSClientBuilder().build(endpoint,keyid,keysecret);
        if(!ossClient.doesBucketExist(bucketname)){
            ossClient.createBucket(bucketname);
            ossClient.setBucketAcl(bucketname, CannedAccessControlList.PublicRead);
        }
        //构建object目录
        //构建日期路径：avatar/2019/02/26/文件名
        String folder = new DateTime().toString("yyyy/MM/dd");

        //文件名：uuid.扩展名
        String fileName = UUID.randomUUID().toString();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String key = module + "/" + folder + "/" + fileName + fileExtension;

        //上传文件流
        ossClient.putObject(bucketname,key,inputStream);
        return "https://" + bucketname + "." + endpoint + "/" + key;
    }

    @Override
    public void removeFile(String url) {
        String endpoint=ossProperties.getEndpoint();
        String keyid=ossProperties.getKeyid();
        String keysecret=ossProperties.getKeysecret();
        String bucketname=ossProperties.getBucketname();

        //创建OSSClient实例
        OSS ossClient=new OSSClientBuilder().build(endpoint,keyid,keysecret);

        String host="https://"+bucketname+"."+endpoint+"/";
        String objectName=url.substring(host.length());

        //删除文件
        ossClient.deleteObject(bucketname,objectName);

        //关闭OSSClient
        ossClient.shutdown();
    }
}
