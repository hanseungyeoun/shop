package com.example.shop.service.file;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class FileStore {

    private final FileUtil fileUtil;

    public List<String> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
        if(multipartFiles.size() == 0) {
            return null;
        }

        List<String> storeFileResult = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                storeFileResult.add(storeFile(multipartFile));
            }
        }
        return storeFileResult;
    }

    public String storeFile(MultipartFile multipartFile) throws IOException {
        if(multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);

        String fullPath = fileUtil.getFullPath(storeFileName);

        multipartFile.transferTo(new File(fullPath));

        return storeFileName;
    }

    private String createStoreFileName(String fileName) {
        String ext = extractExt(fileName);
        String uuid = UUID.randomUUID().toString();
        return uuid + ext;
    }

    private String extractExt(String fileName) {
        int pos = fileName.lastIndexOf(".");
        String ext = fileName.substring(pos);
        return ext;
    }
}