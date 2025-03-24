package org.example.springboot.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface ImageRecognitionService {

    /**
     * 图像识别方法
     *
     * @param file 上传的图片文件
     * @return 识别结果
     * @throws IOException
     */
    String recognizeImage(MultipartFile file) throws IOException;
}
