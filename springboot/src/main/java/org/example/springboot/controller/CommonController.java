//package org.example.springboot.controller;
//
////import com.sky.constant.MessageConstant;
////import com.sky.result.Result;
////import com.sky.utils.AliOssUtil;
////import io.swagger.annotations.Api;
////import io.swagger.annotations.ApiOperation;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import io.swagger.v3.oas.annotations.tags.Tags;
//import jakarta.annotation.Resource;
//import lombok.extern.slf4j.Slf4j;
//import org.example.springboot.domain.Result;
//import org.example.springboot.utils.AliOssUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.UUID;
//
///**
// * 通用接口
// */
//@RestController
//@RequestMapping("/admin/common")
//@Tag(name = "通用接口")
//@Slf4j
//public class CommonController {
//
//    @Resource
//    private AliOssUtil aliOssUtil;
//
//    /**
//     * 文件上传
//     * @param file
//     * @return
//     */
//    @PostMapping("/upload")
//    @Operation(summary = "文件上传", description = "文件上传", method = "POST")
//    public Result<String> upload(MultipartFile file) {
//        log.info("文件上传：{}", file);
//        try {
//            //原始文件名
//            String originalFilename = file.getOriginalFilename();
//            //截取原始文件名的后缀
//            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
//            //构造新文件名称
//            String objectName = UUID.randomUUID().toString() + extension;
//            //文件的请求路径
//            String filePath = aliOssUtil.upload(file.getBytes(), objectName);
//            return Result.success(filePath);
//        } catch (IOException e) {
//            log.error("文件上传失败：{}", e.getMessage());
//        }
//        return Result.error("文件上传失败");
//    }
//}
