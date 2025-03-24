package org.example.springboot.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.springboot.service.ImageRecognitionService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
@Slf4j
public class ImageRecognitionServiceImpl implements ImageRecognitionService {

    /**
     * 图像识别方法
     *
     * @param file 上传的图片文件
     * @return 识别结果
     * @throws IOException
     */
    public String recognizeImage(MultipartFile file) throws IOException {
        try {
            // 指定 Python 解释器路径
            String pythonInterpreter = "D:\\vscodeProjexts\\fossilRecognition\\.venv\\Scripts\\python.exe"; // 根据实际情况修改
            // 指定 Python 脚本位置
            String pythonScript = "D:\\test.py"; // 根据实际情况修改

            // 创建临时文件
            File tempFile = File.createTempFile("uploaded_image", ".jpg");
            // 将上传的文件保存到临时文件
            file.transferTo(tempFile);
            // 获取临时文件的路径
            String imagePath = tempFile.getAbsolutePath();
            log.info("获取到文件：{}", imagePath);


            // 构建 Python 命令
            ProcessBuilder processBuilder = new ProcessBuilder(pythonInterpreter, pythonScript, imagePath);
            Process process = processBuilder.start();

            // 读取 Python 脚本的输出
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }

            // 等待 Python 脚本执行完毕
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                return output.toString();
            } else {
                return "图像识别失败";
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "发生错误：" + e.getMessage();
        }


    }
}
