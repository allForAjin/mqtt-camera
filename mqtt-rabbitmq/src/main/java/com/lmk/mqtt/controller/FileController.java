package com.lmk.mqtt.controller;

import com.lmk.mqtt.entity.UploadFile;
import com.lmk.mqtt.entity.result.ResponseResult;
import com.lmk.mqtt.utils.HttpClientUtil;
import com.lmk.mqtt.utils.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/resource/file")
public class FileController {
    private Logger logger = LoggerFactory.getLogger(FileController.class);

    @GetMapping
    public void videoDownload(@RequestParam("path") String videoPath,
                              HttpServletResponse response) throws IOException {
        File file = new File(videoPath);
        if (!file.exists()) {
            logger.error("video not existed,path---{}", videoPath);
            ResponseUtil.response(response, ResponseResult.fail("文件不存在"));
            return;
        }
        InputStream inputStream = Files.newInputStream(file.toPath());
        String fileName = file.getName();
        response.setContentType("application/octet-stream");
        response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
        OutputStream outputStream = response.getOutputStream();
        byte[] bytes = new byte[2048];
        int len;
        while ((len = inputStream.read(bytes)) > 0) {
            outputStream.write(bytes, 0, len);
        }
        inputStream.close();
    }

    @PostMapping("/upload")
    public String upload(MultipartHttpServletRequest multiRequest) throws IOException {
        List<MultipartFile> fileList = multiRequest.getFiles("file");
        for (MultipartFile file:fileList){
            if (!file.isEmpty()){
                String path = "C:\\Users\\Administrator.Win10-2022YZGQX\\Desktop\\upload\\";

                String originalFilename = file.getOriginalFilename();
                File uploadFile = new File(path+originalFilename);
                file.transferTo(uploadFile);
            }
        }
        return "success";
    }
}
