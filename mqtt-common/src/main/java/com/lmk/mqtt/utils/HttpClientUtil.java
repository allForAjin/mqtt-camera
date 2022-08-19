package com.lmk.mqtt.utils;

import com.lmk.mqtt.entity.UploadFile;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HttpClientUtil {
    private static final CloseableHttpClient httpClient;

    private static final String CHARSET_UTF8 = "UTF-8";

    static {
        RequestConfig config = RequestConfig.custom().setConnectTimeout(60000).setSocketTimeout(15000).build();
        httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
    }
    public static String doPostFile(String url, List<UploadFile> fileList){
        HttpPost httpPost = new HttpPost(url);
        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
        entityBuilder.setCharset(StandardCharsets.UTF_8)
                .setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        for (UploadFile uploadFile:fileList){
            File file = new File(uploadFile.getPath());
            try {
                InputStream fileStream = new FileInputStream(file);
                entityBuilder.addBinaryBody("file",fileStream, ContentType.APPLICATION_OCTET_STREAM,uploadFile.getFileName());
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        httpPost.setEntity(entityBuilder.build());
        try {
            CloseableHttpResponse response = httpClient.execute(httpPost);
            HttpEntity resultEntity = response.getEntity();
            String result = null;
            if (resultEntity != null) {
                result = EntityUtils.toString(resultEntity, CHARSET_UTF8);
            }
            response.close();
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
