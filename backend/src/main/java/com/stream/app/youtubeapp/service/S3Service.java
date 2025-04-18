package com.stream.app.youtubeapp.service;

import io.awspring.cloud.s3.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Utilities;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.net.URL;
import java.util.UUID;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class S3Service implements FileService {

    public static final String VIDEOBUCKET_YOUTUBE = "videobucket-youtube";
    private final S3Client s3Client;

    @Override
    public String uploadFile(MultipartFile multipartFile) {
        //prepare a key
        var fileNameExt = StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());
        var key = UUID.randomUUID().toString() +"." +fileNameExt;

        try {

            var requestBody = RequestBody.fromInputStream(multipartFile.getInputStream(), multipartFile.getSize());

            var response = s3Client.putObject(request -> request
                            .bucket(VIDEOBUCKET_YOUTUBE)
                            .key(key)
                            .contentLength(multipartFile.getSize())
                            .contentType(multipartFile.getContentType())
                            .acl(ObjectCannedACL.PUBLIC_READ)
                    , requestBody);


            System.out.println("Upload successful! ETag: " + response.eTag());
            S3Utilities utilities = s3Client.utilities();
            URL url = utilities.getUrl(GetUrlRequest.builder().bucket(VIDEOBUCKET_YOUTUBE).key(key).build());

            return url.toString();

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An Exception occured while uploading file " + key);
        }
    }
}
