package com.example.shop.service.file;

import com.example.shop.config.DownloadProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@RequiredArgsConstructor
@Slf4j
public class FileUtil {

    private final DownloadProperties myProperties;

    public String getFullPath(String fileName){
        return myProperties.getDefaultPath() + fileName;
    }

    public boolean deleteUploadFile(String uploadFileName) {
        File file = new File(getFullPath(uploadFileName));
        boolean result = false;

        if(file.exists()) {
            if (file.delete()) {
                log.info("파일삭제 성공");
                result = true;
            } else {
                log.info("파일삭제 성공");
            }
        }

        return true;
    }
}
