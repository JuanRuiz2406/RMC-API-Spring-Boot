package com.api.ReportsMyCity.file;

import com.api.ReportsMyCity.file.FileServiceAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/files")
public class File {
    @Autowired
    private FileServiceAPI fileServiceAPI;
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFiles(@RequestParam("files") List<MultipartFile> files){
        try {
            fileServiceAPI.save((MultipartFile) files);
            return ResponseEntity.status(HttpStatus.OK).body("Los archivos fueron cargados correctamete al servidor");
        }catch (Exception e){
            System.out.println(e.getStackTrace());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
