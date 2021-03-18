package com.api.ReportsMyCity.file;

import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public interface FileServiceAPI {
    public void save(MultipartFile file) throws Exception;
    public Resource load (String name) throws Exception;
    public void saT(List<MultipartFile> file) throws Exception;
    public Stream<Path> loadAll() throws Exception;
}
