package com.ithd.chat.chatapp.storage;

import com.ithd.chat.chatapp.util.DecodingEncoding;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FileService {

    @Value("${app.upload.dir:${user.home}}")
    public String uploadDir;
    public final DecodingEncoding decodingEncoding;

    public ResponseEntity<?> uploadFile(MultipartFile file) {
        Map<String, Object> body = new HashMap<>();
        try {
            String originalFileName = file.getOriginalFilename();
            String extension = "."+FilenameUtils.getExtension(originalFileName);

            String encodedFileName = decodingEncoding.encodeValue(file.getOriginalFilename()+"fertifa");
            Path copyLocation = Paths.get(uploadDir + File.separator + "/jvm/apache-tomcat-9.0.27/domains/chat.fertifabenefits.com/ROOT/upload/"+ StringUtils.cleanPath(encodedFileName)+extension);
            //Path copyLocation = Paths.get(uploadDir + File.separator + StringUtils.cleanPath(encodedFileName) + extension);
            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
            String finalLocation = "upload/" + encodedFileName +extension;
            body.put("documentUrlLink", finalLocation);
            body.put("documentName", originalFileName);
            body.put("documentType", file.getContentType());
            body.put("documentSize", file.getSize());
            return new ResponseEntity<>(body, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}