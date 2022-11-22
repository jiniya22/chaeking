package com.chaeking.api.util;

import com.chaeking.api.domain.value.ChaekingProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
public class FileUtils {

    public static String uploadImageFile(MultipartFile profileImage) {
        if(profileImage == null)
            return null;
        try {
            File folder = new CustomFile();
            if(!folder.exists())
                folder.mkdirs();

            String orgName = profileImage.getOriginalFilename();
            if(Strings.isBlank(orgName)) return null;
            String fileName = createFilename() + orgName.substring(orgName.lastIndexOf(".")).toLowerCase();
            File file = new CustomFile(fileName);
            profileImage.transferTo(file);
            return fileName;
        } catch(IOException e) {
            log.error("이미지 업로드 중 에러 발생 : {}", e.getClass().getName());
        }
        return null;
    }

    private static String createFilename() {
        String uuid = null;
        do {
            uuid = UUID.randomUUID().toString();
        } while(new CustomFile(uuid).exists());
        return uuid;
    }

    public static void removeImageFile(String oldImageUrl) {
        if(Strings.isBlank(oldImageUrl)) return;
        File oldFile = new CustomFile(oldImageUrl.replace(ChaekingProperties.getImageUrlPrefix(), ""));
        if(oldFile.exists())
            oldFile.delete();
    }

    static class CustomFile extends File {
        public CustomFile() {
            super(ChaekingProperties.getImageUploadPath());
        }
        public CustomFile(String fileName) {
            super(ChaekingProperties.getImageUploadPath() + fileName);
        }
    }
}
