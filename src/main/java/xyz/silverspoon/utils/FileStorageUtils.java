package xyz.silverspoon.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import xyz.silverspoon.service.impl.ImMessageServiceImpl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;

@Slf4j
@Component
public class FileStorageUtils {

    @Autowired
    private UUIDGenerator uuidGenerator;

    @Value(value = "${file.upload.path}")
    private String FILEUPLOADPATH;

    /**
     *
     * @param file 从Controller传过来的文件数据
     * @param type 文件保存的类型
     * @return ""：表示文件保存失败。否则返回文件保存的相对路径
     */
    public String saveFile(MultipartFile file, UUIDType type) {
        log.info(file.getContentType());
        String pathPrefix;
        switch (type) {
            case IM_FILE:
                pathPrefix = "/files/";
                break;
            case IM_PIC:
                pathPrefix = "/imgs/";
                break;
            default:
                pathPrefix = "";
        }
        String filename = pathPrefix + uuidGenerator.generateUUID(type);
        File temp = new File(FILEUPLOADPATH + filename);
        if (!temp.exists()) {
            try {
                temp.createNewFile();
            } catch (IOException e) {
                log.error("文件路径：{}", temp.getAbsolutePath() + temp.getName());
                log.error("{},{}", ImMessageServiceImpl.class, e.getMessage());
                return "";
            }
        }
        try {
            file.transferTo(temp);
            return filename;
        } catch (IOException e) {
            log.error(e.getMessage());
            return "";
        }
    }

    public Resource loadFileAsResource(String filepath) {
        try {
            File file = new File(FILEUPLOADPATH + filepath);
            Resource resource = new UrlResource(file.toURI());
            if (resource.exists()) {
                return resource;
            }
            log.info("文件路径{}", file.getAbsolutePath());
        } catch (MalformedURLException e) {
            log.error("创建资源文件失败, {}", e.getMessage());
        }
        return null;
    }
}
