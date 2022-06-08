package com.optus.infosec.api.service.wrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author SM
 *
 * Class to make rest calls to File Service
 */
@Service
public class FileService {

    @Autowired
    private RestTemplateBuilder restTemplate;

    @Value("${file.server.host}")
    private String fileServerHost;

    @Value("${file.upload}")
    private String fileUpload;

    @Value("${file.download}")
    private String fileDownload;

    @Value("${file.download.zip}")
    private String fileDownloadZip;

    @Value("${file.list}")
    private String fileList;

    /**
     * Uploads file to file server
     *
     * @param path
     * @param file
     */
    public void upload(Path path, MultipartFile file) {

        String url = fileServerHost + fileUpload;

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "multipart/form-data");

        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("path", path.toString());

        try {
            body.add("file", new ByteArrayResource(file.getBytes()){
                @Override
                public String getFilename(){
                    return file.getOriginalFilename();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        restTemplate.build().postForObject(url, requestEntity, Void.class, uriVariables);
    }

    /**
     * Download file from server
     *
     * @param path
     * @param fileName
     * @return
     */
    public Resource download(Path path, String fileName) {

        String url = fileServerHost + fileDownload;

        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("path", path.toString());
        uriVariables.put("fileName", fileName);

        byte[] fileBytes = restTemplate.build().getForObject(url, byte[].class, uriVariables);
        return new ByteArrayResource(fileBytes);
    }

    /**
     * Get file list
     * @param path
     * @return
     */
    public List<String> list(Path path){

        String baseUrl = fileServerHost;
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("path", path.toString());

        String pathUrl = UriComponentsBuilder.fromPath(fileList).buildAndExpand(uriVariables).encode().toUriString();


        String[] fileList = restTemplate.build().getForObject(baseUrl+pathUrl, String[].class);
        return Arrays.asList(fileList);
    }

    /**
     * Download folder as zip
     * @param path
     * @return
     */
    public byte[] downloadAsZip(Path path) {

        String url = fileServerHost + fileDownloadZip;

        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("path", path.toString());

        byte[] fileBytes = restTemplate.build().getForObject(url, byte[].class, uriVariables);
        return fileBytes;
    }
}
