package com.optus.infosec.api.service;

import com.optus.infosec.api.dto.CustomResponse;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LocalFileService {

    private static final Logger LOG = LoggerFactory.getLogger(LocalFileService.class);

    String fileSaveLocation = System.getProperty("sra.file.save.location");

    public ResponseEntity<List<String>> listFiles(Long engagementID) {
        try {
            Collection<File> files = FileUtils.listFiles(new File(fileSaveLocation + engagementID), null, true);
            List<String> fileNameSet = files.stream().map(File::getName).collect(Collectors.toList());
            return new ResponseEntity<>(fileNameSet, HttpStatus.OK);
        } catch (Exception e) {
            LOG.error(">>>>>>>>>>>>>>>>>>>>>>: method >>  listFiles , Exception:  {}", e.getMessage());
        }
        return new ResponseEntity<>(Collections.EMPTY_LIST, HttpStatus.OK);
    }

    public void createZipFile(Long engagementId) {
        LOG.debug(">>>>>>>>>>>>>> Method >>> createZipFile(), Variable: engagementId, Value: {}", engagementId);
        // Create zip file stream.
        try (ZipArchiveOutputStream archive = new ZipArchiveOutputStream(new FileOutputStream(fileSaveLocation + engagementId + ".zip"))) {
            File folderToZip = new File(fileSaveLocation + engagementId);
            // Walk through files, folders & sub-folders.
            Files.walk(folderToZip.toPath()).forEach(path -> {
                File file = path.toFile();
                // Directory is not streamed, but its files are streamed into zip file with
                // folder in it's path
                if (!file.isDirectory()) {
                    LOG.debug(">>>>>>>>>>>>>> Method >>> createZipFile() >>> compressing file:  {},", file);
                    ZipArchiveEntry entry_1 = new ZipArchiveEntry(file, file.toString());
                    try (FileInputStream fis = new FileInputStream(file)) {
                        archive.putArchiveEntry(entry_1);
                        IOUtils.copy(fis, archive);
                        archive.closeArchiveEntry();
                    } catch (IOException e) {
                        LOG.error(">>>>>>>>>>>>>>>>>>>>>>: method >>  createZipFile, Exception:  {}", e.getMessage());
                    }
                }
            });
            // Complete archive entry addition.
            archive.finish();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] getZipFileByteArrayForEngagement(Long engagementId) {
        this.createZipFile(engagementId);
        byte[] fileByteArray = null;
        LOG.debug(">>>>>>>>>>>>>> Method >>> getZipFileForEngagement(), Variable: engagementId, Value: {}", engagementId);
        try {
            fileByteArray = FileUtils.readFileToByteArray(new File(fileSaveLocation + engagementId + ".zip"));
            LOG.debug(">>>>>>>>>>>>>> Method >>> getZipFileForEngagement(), deleting zip file >>> {}", fileSaveLocation + engagementId + ".zip");
            boolean deleted = FileUtils.deleteQuietly(new File(fileSaveLocation + engagementId + ".zip"));
            if (!deleted) {
                LOG.error(">>>>>>>>>>>>>>>>>>>>>>: method >>  getZipFileForEngagement, failed to delete zip file:  {}", fileSaveLocation + engagementId + ".zip");
            }
        } catch (Exception e) {
            LOG.error(">>>>>>>>>>>>>>>>>>>>>>: method >>  getZipFileForEngagement, Exception:  {}", e.getMessage());
        }
        return fileByteArray;
    }

    public ResponseEntity<Object> upload(Long engagementId, String engagementFormType, MultipartFile file) {
        Optional<MultipartFile> optionalFile = Optional.ofNullable(file);
        if (folderAvailableForEngagementId(engagementId)) {
            if (!folderAvailableForEngagementFormType(engagementId, engagementFormType)) {
                createFolderNameAsEngagementFormType(engagementId, engagementFormType);
            }
            return saveFile(engagementId, engagementFormType, optionalFile);
        } else {
            createNewFolderNamedEngagementId(engagementId);
            createFolderNameAsEngagementFormType(engagementId, engagementFormType);
            return saveFile(engagementId, engagementFormType, optionalFile);
        }
    }

    private ResponseEntity<Object> saveFile(Long engagementId, String engagementFormType, Optional<MultipartFile> multipartFile) {
        LOG.debug(">>>>>>>>>>>>>> Method >>> saveFile(), Variable: engagementId, Value: {}", engagementId);
        LOG.debug(">>>>>>>>>>>>>> Method >>> saveFile(), Variable: engagementFormType, Value: {}", engagementFormType);
        if (multipartFile.isPresent()) {
            LOG.debug(">>>>>>>>>>>>>> Method >>> saveFile(), Variable: multipartFile, Value: {}", multipartFile.get().getName());
            File file;
            try {
                String filename = multipartFile.get().getOriginalFilename();
                if (fileAvailableInTheDisk(engagementId, engagementFormType, filename)) {
                    filename = UUID.randomUUID() + "_" + filename;
                }
                file = new File(fileSaveLocation + engagementId + "/" + engagementFormType, filename);
                FileUtils.writeByteArrayToFile(file, multipartFile.get().getBytes());
                LOG.info("File Saved Successfully !, File Name: {}", file.getName());
            } catch (Exception e) {
                LOG.error("Unable to save file, {}", e.getMessage());
                return new ResponseEntity<>(new CustomResponse("Unable to save file, Please contact support !"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            LOG.info("No file received to save !");
            return new ResponseEntity<>(new CustomResponse("No file received to save !"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new CustomResponse("File Saved Successfully !"), HttpStatus.CREATED);
    }


    private void createFolderNameAsEngagementFormType(Long engagementId, String engagementFormType) {
        File file = new File(fileSaveLocation + engagementId + "/" + engagementFormType);
        boolean engagementTypeDirectoryCreated = file.mkdir();
        if (!engagementTypeDirectoryCreated) {
            LOG.error("Failed to create directory for engagement type: {}", engagementFormType);
        }
    }

    private boolean folderAvailableForEngagementFormType(Long engagementId, String engagementFormType) {
        return new File(fileSaveLocation + engagementId + "/" + engagementFormType).exists();
    }

    private void createNewFolderNamedEngagementId(Long engagementId) {
        File file = new File(fileSaveLocation + engagementId);
        boolean engagementIdDirectoryCreated = file.mkdir();
        if (!engagementIdDirectoryCreated) {
            LOG.error("Failed to create directory for engagement id: {}", engagementId);
        }
    }

    public boolean folderAvailableForEngagementId(Long engagementId) {
        return new File(fileSaveLocation + engagementId).exists();
    }

    public boolean fileAvailableInTheDisk(Long engagementId, String engagementFormType, String fileName) {
        return new File(fileSaveLocation + engagementId + "/" + engagementFormType + "/" + fileName).exists();
    }


}
