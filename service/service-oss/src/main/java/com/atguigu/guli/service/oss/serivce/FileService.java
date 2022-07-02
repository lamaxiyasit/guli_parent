package com.atguigu.guli.service.oss.serivce;

import java.io.InputStream;

public interface FileService {
    /**
     * 阿里云oss文件上传
     * @param inputStream 输入流
     * @param module 文件夹名称
     * @param originalFileName 原始文件名
     * @return 文件在oss服务器上url地址
     */
    String upload(InputStream inputStream, String module, String originalFileName);

    /**
     * 删除阿里云头像
     * @param url 头像的路径
     */
    void removeFile(String url);
}
