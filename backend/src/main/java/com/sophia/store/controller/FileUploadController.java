package com.sophia.store.controller;

import com.philosophy.base.util.FilesUtils;
import com.philosophy.image.util.ImageUtils;
import com.sophia.store.entity.vo.Response;
import com.sophia.store.utils.Constant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

@RestController
@Slf4j
@Api(value = "文件上传接口", tags = {"文件上传"})
public class FileUploadController {

    @Value(("${upload_file}"))
    private String upload_images;

    /**
     * 允许的文件类型
     */
    private static final String[] ALLOWED_EXTENSIONS = {
            "jpg", "png", "gif", "bmp"
    };


    @SneakyThrows
    @PostMapping("/upload")
    @ApiOperation(value = "上传文件")
    public Response singleFileUpload(@RequestParam("file") MultipartFile file) {
        Response response = new Response();
        String originFileName = file.getOriginalFilename();
        log.info("origin name is {}", originFileName);
        if (originFileName != null) {
            String extendName = FilesUtils.getExtension(Paths.get(originFileName));
            log.info("extend name is {}", extendName);
            boolean flag = false;
            for (String ext : ALLOWED_EXTENSIONS) {
                if (ext.equalsIgnoreCase(extendName)) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                response.setCode(Constant.NOK);
                response.setMessage("文件扩展名必须是" + Arrays.toString(ALLOWED_EXTENSIONS));
            } else {
                String fileName = originFileName.split("\\.")[0];
                String fileFullName = fileName + "." + extendName;
                Path serverFile = Paths.get(upload_images, fileFullName);
                Path uploadImages = Paths.get(upload_images);
                if (!Files.exists(uploadImages)) {
                    Files.createDirectories(uploadImages);
                }
                try {
                    file.transferTo(serverFile);
                    // 100K大小
                    long maxFileSize = 100 * 1024;
                    log.info("serverFile size = {}", serverFile.toFile().length());
                    String newFileName = fileName + "_scale" + "." + extendName;
                    Path targetFile = Paths.get(upload_images, newFileName);
                    ImageUtils imageUtils = new ImageUtils();
                    if (serverFile.toFile().length() < maxFileSize) {
                        // 不做任何处理，只是修改一个名字
                        imageUtils.scale(serverFile, targetFile, 1, false);
                    } else {
                        // 缩减文件直到小于100K，基本上10M左右的文件2次能缩小到100K
                        imageUtils.scale(serverFile, targetFile, 2, false);
                        while (targetFile.toFile().length() > maxFileSize) {
                            log.info("scale target file again");
                            imageUtils.scale(targetFile, targetFile, 2, false);
                        }
                    }
                    log.info("serverFile path is {}", targetFile.toAbsolutePath().toString());
                    response.setData("/images/" + targetFile.getFileName().toString());
                    response.setMessage("upload file success");
                } catch (IOException | IllegalStateException e) {
                    response.setCode(Constant.SERVER_ERROR);
                    response.setMessage(e.getMessage());
                }
            }
        } else {
            response.setCode(Constant.NOK);
            response.setMessage("文件不正确");
        }
        return response;
    }
}
