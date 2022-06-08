package com.optus.infosec;

import com.optus.infosec.api.service.LocalFileService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.util.List;


class LocalFileServiceTest {

    @BeforeEach
    @Disabled
    void setUp() {
        System.setProperty("sra.file.save.location", "/home/mihan/Desktop/");
        System.setProperty("sra.test.upload.file", "/home/mihan/Desktop/test_upload.doc");
    }

    @Test
    @Disabled
    void folderAvailableAsEngagementId() {
        LocalFileService localFileService = new LocalFileService();

        boolean exist = localFileService.folderAvailableForEngagementId(1L);
        Assertions.assertFalse(exist);

        File file = new File(System.getProperty("sra.file.save.location") + "2");
        boolean mkdir = file.mkdir();
        if (mkdir) {
            boolean exist2 = localFileService.folderAvailableForEngagementId(2L);
            Assertions.assertTrue(exist2);
            boolean delete = file.delete();
        }
    }

    @Test
    @Disabled
    void upload() {
        LocalFileService localFileService = new LocalFileService();
        MockMultipartFile multipartFile
                = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );
        localFileService.upload(1L, "FORM1", multipartFile);
        localFileService.upload(1L, "FORM1", multipartFile);
        localFileService.upload(1L, "FORM1", multipartFile);
        localFileService.upload(1L, "FORM2", multipartFile);
        localFileService.upload(1L, "FORM2", multipartFile);
        localFileService.upload(1L, "FORM2", multipartFile);
        localFileService.upload(1L, "FORM2", multipartFile);
        localFileService.upload(2L, "FORM2", multipartFile);
        localFileService.upload(2L, "FORM2", multipartFile);
        localFileService.upload(1L, "FORM2", multipartFile);
        localFileService.upload(4L, "FORM2", multipartFile);
        localFileService.upload(1L, "FORM2", multipartFile);
        localFileService.upload(6L, "FORM2", null);
    }

    @Test
    @Disabled
    void listFiles() {
        LocalFileService localFileService = new LocalFileService();
        ResponseEntity<List<String>> listResponseEntity = localFileService.listFiles(1L);
        System.out.println(listResponseEntity.getBody());
    }

    @Test
    @Disabled
    void createFiles() {
        LocalFileService localFileService = new LocalFileService();
        localFileService.createZipFile(1L);
    }

    @Test
    @Disabled
    void getZipFileByteArrayForEngagement() {
        LocalFileService localFileService = new LocalFileService();
        localFileService.getZipFileByteArrayForEngagement(1L);
    }


}
