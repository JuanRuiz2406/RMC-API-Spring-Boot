package com.api.ReportsMyCity.file;

import com.api.ReportsMyCity.file.FileServiceAPI;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

@Service
public class FileServiceImpl implements FileServiceAPI {

    private final Path rootFolder = Paths.get("uploads");

    @Override
    public void save(MultipartFile file) throws Exception {
        Files.copy(file.getInputStream(), this.rootFolder.resolve(file.getOriginalFilename()));
    }

    @Override
    public Resource load(String name) throws Exception {
        return null;
    }

    @Override
    public void saT(List<MultipartFile> files) throws Exception {
        for (MultipartFile file : files){
            this.save(file);
        }
    }

    @Override
    public Stream<Path> loadAll() throws Exception {
        return null;
    }
}
