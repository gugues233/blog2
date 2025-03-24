//package org.example.springboot.controller;
//
//import io.swagger.v3.oas.annotations.Operation;
//import jakarta.annotation.Resource;
//import lombok.extern.slf4j.Slf4j;
//import org.example.springboot.domain.Result;
//import org.example.springboot.service.ImageRecognitionService;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//@Slf4j
//@RestController
//@RequestMapping("/image")
//public class ImageRecognitionController {
//
//    @Resource
//    private ImageRecognitionService imageRecognitionService;
//
//    /**
//     * 上传图片并进行图像识别
//     *
//     * @param file 上传的图片文件
//     * @return 识别结果
//     */
//    @PostMapping("/recognition")
//    @Operation(summary = "上传图片并进行图像识别", description = "上传图片并进行图像识别", method = "POST")
//    public Result<String> uploadImage(@RequestParam("file") MultipartFile file) {
//        log.info("接收到前端上传图片：{}", file);
//        log.info("尝试处理 /image/recognition 请求");
//        try {
//            // 调用图像识别服务
//            String result = imageRecognitionService.recognizeImage(file);
//            return Result.success(result);
//        } catch (Exception e) {
//            log.error("图像识别失败", e);
//            return Result.error("图像识别失败");
//        }
//    }
//}

package org.example.springboot.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.springboot.domain.Result;
import org.example.springboot.domain.vo.RecognitionVo;
import org.example.springboot.service.ImageRecognitionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/image")
public class ImageRecognitionController {

    @Resource
    private ImageRecognitionService imageRecognitionService;

    /**
     * 上传图片并进行图像识别
     *
     * @param file 上传的图片文件
     * @return 识别结果
     */
    @PostMapping("/recognition")
    @Operation(summary = "上传图片并进行图像识别", description = "上传图片并进行图像识别", method = "POST")
    public Result<RecognitionVo> uploadImage(@RequestParam("file") MultipartFile file) {
        log.info("接收到前端上传图片：{}", file);
        log.info("尝试处理 /image/recognition 请求");
        try {
            // 调用图像识别服务
            String result = imageRecognitionService.recognizeImage(file);
            RecognitionVo vo = RecognitionVo.builder().result(result).build();
            return Result.success(vo);
        } catch (Exception e) {
            log.error("图像识别失败", e);
            return Result.error("图像识别失败");
        }
    }
}

