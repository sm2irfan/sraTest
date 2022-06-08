package com.optus.infosec.api.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * @author SM
 *
 * Interface for Storage
 *
 */
public interface StorageService {

    void init();

    void store(MultipartFile file, Path directoryPath);

    Stream<Path> loadAll(Path directoryPath);

    Path load(String filename, Path directoryPath);

    Resource loadAsResource(String filename, Path directoryPath);

    void deleteAll();

    Resource loadAllFilesAsZipResource(Path sourceDirectoryPath, Path zipDirectoryPath);
}
