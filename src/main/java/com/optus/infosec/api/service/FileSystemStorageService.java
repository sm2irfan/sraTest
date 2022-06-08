package com.optus.infosec.api.service;

import com.optus.infosec.api.exception.StorageException;
import com.optus.infosec.api.exception.StorageFileNotFoundException;
import com.optus.infosec.api.util.ApplicationConstants;
import com.optus.infosec.api.util.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Service
public class FileSystemStorageService implements StorageService {

    @Value("${file.upload.root.location}")
    private Path fileUploadRootLocation;

    @Override
    public void store(MultipartFile file, Path directoryPath) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
            }
            //check if path exists
            Path fullPathOfFile = Paths.get(fileUploadRootLocation.toString(), directoryPath.toString());
            if(Files.notExists(fullPathOfFile)){
                Files.createDirectories(fullPathOfFile);
            }

            Files.copy(file.getInputStream(), fullPathOfFile.resolve(file.getOriginalFilename()));
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

    @Override
    public Stream<Path> loadAll(Path directoryPath) {

        //check if path exists
        Path fullPathOfFiles = Paths.get(fileUploadRootLocation.toString(), directoryPath.toString());
        try {
            return Files.walk(fullPathOfFiles, 1)
                    .filter(path -> !path.equals(fullPathOfFiles))
                    .map(path -> fullPathOfFiles.relativize(path));
        } catch (IOException e) {
            //throw new StorageException("Failed to read stored files", e);
            return Stream.of(Paths.get(""));
        }
    }

    @Override
    public Path load(String filename, Path directoryPath) {
        Path fullPathOfFile = Paths.get(fileUploadRootLocation.toString(), directoryPath.toString());
        return fullPathOfFile.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename, Path directoryPath) {
        try {
            Path file = load(filename, directoryPath);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException("Could not read file: " + filename);

            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        //FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public Resource loadAllFilesAsZipResource(Path sourceDirectoryPath, Path zipDirectoryPath) {

        String zipFileName = sourceDirectoryPath.getFileName().toString()+ ApplicationConstants.DOT +ApplicationConstants.ZIP;
        Path fullPathOfZipFile = Paths.get(fileUploadRootLocation.toString(), zipDirectoryPath.toString(), zipFileName);
        Path fullPathToZipFile = Paths.get(fileUploadRootLocation.toString(), zipDirectoryPath.toString());

        // delete zip if already exists
        if(Files.exists(fullPathOfZipFile)){
            FileSystemUtils.deleteRecursively(fullPathOfZipFile.toFile());
        }

        // create folder of zip if doesn't exists
        if(Files.notExists(fullPathToZipFile)){
            try {
                Files.createDirectories(fullPathToZipFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // create zip
        Path fullPathOfSource = Paths.get(fileUploadRootLocation.toString(), sourceDirectoryPath.toString());
        try {
            FileUtil.createZIP(fullPathOfSource.toString(), fullPathOfZipFile.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loadAsResource(zipFileName, zipDirectoryPath);
    }

    @Override
    //@PostConstruct
    public void init() {
        try {
            if(Files.notExists(fileUploadRootLocation)) {
                Files.createDirectory(fileUploadRootLocation);
            }
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}