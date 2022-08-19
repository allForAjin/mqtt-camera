package com.lmk.mqtt.client;

import com.lmk.mqtt.entity.UploadFile;
import com.lmk.mqtt.utils.HttpClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.List;
import java.util.concurrent.*;

//@Component
public class FileSender {
    @Value("${upload.picture.path}")
    private String picturePath;

    @Value("${upload.picture.upload-url}")
    private String pictureUploadUrl;

    @Value("${upload.video.path}")
    private String videoPath;

    @Value("${upload.video.upload-url}")
    private String videoUploadUrl;

    @Value("${upload.video.save-path}")
    private String videoSavePath;

    private Logger logger = LoggerFactory.getLogger(FileSender.class);

    private final List<UploadFile> pictureList = new CopyOnWriteArrayList<>();

    private final List<UploadFile> videoList = new CopyOnWriteArrayList<>();

    private final StringBuilder sb = new StringBuilder();

    @PostConstruct
    public void setUp() {
        pictureDetect();
        videoDetect();
    }

    public void pictureDetect() {
        File pictureDir = getDir(picturePath);
        Thread pictureThread = new Thread(() -> {
            logger.info("picture-detect-thread start");
            while (true) {
                File[] pictures = pictureDir.listFiles();
                if (pictures != null && pictures.length > 0) {
                    pictureList.clear();
                    for (File picture : pictures) {
                        pictureList.add(new UploadFile(picture.getName(), picture.getAbsolutePath()));
                    }
                    try {
                        HttpClientUtil.doPostFile(pictureUploadUrl, pictureList);
                        logger.info("send pictures,count:{},pictureList:{}", pictureList.size(), pictureList);
                    } catch (Exception e) {
                        logger.error("post files error:{}", e.getMessage());
                        e.printStackTrace();
                        continue;
                    }
                    for (File picture : pictures) {
                        picture.delete();
                    }
                }
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "picture-detect-thread");
        pictureThread.start();
    }

    public void videoDetect() {
        File videoDir = getDir(videoPath);
        File videoSaveDir = getDir(videoSavePath);
        Thread videoThread = new Thread(() -> {
            while (true) {
                File[] videos = videoDir.listFiles();
                if (videos != null && videos.length > 0) {
                    videoList.clear();
                    for (File video : videos) {
                        videoList.add(new UploadFile(video.getName(), video.getAbsolutePath()));
                    }
                    try {
                        HttpClientUtil.doPostFile(videoUploadUrl, videoList);
                        logger.info("send videos,count:{},videoList:{}", videoList.size(), videoList);
                    } catch (Exception e) {
                        logger.error("post videos error:{}", e.getMessage());
                        e.printStackTrace();
                        continue;
                    }
                    for (File video : videos) {
                        sb.append(videoSavePath)
                                .append("/")
                                .append(video.getName());
                        boolean rename = video.renameTo(new File(sb.toString()));
                        if (rename) {
                            logger.info("move video success,source:{},dest:{}", video.getAbsolutePath(),sb);
                        }
                        sb.delete(0,sb.length());
                    }
                    try {
                        TimeUnit.SECONDS.sleep(10);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

        }, "video-detect-thread");
        videoThread.start();

    }

    private File getDir(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdir();
            return dir;
        }
        if (!dir.isDirectory()) {
            logger.warn("The path is not a Dir,path:{}", path);
            if (dir.delete() && dir.mkdir()) {
                logger.warn("Create Dir :{}", path);
            }
        }
        return dir;
    }
}
