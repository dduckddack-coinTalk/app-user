package com.cointalk.user.service;


import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;

@RequiredArgsConstructor
@Service
public class AwsUploadService {

    private final AmazonS3Client amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public Mono<Boolean> uploadImage(Part partData) {
        String fileName = ((FilePart) partData).filename();
        return partData.content().map(dataBuffer -> {
            File tempFile = null;
            try {
                byte[] byteData = dataBuffer.asInputStream().readAllBytes();
                tempFile = new File(fileName);
                FileCopyUtils.copy(byteData, tempFile);
                PutObjectRequest uploadFileObject = new PutObjectRequest(bucketName, fileName, tempFile)
                        .withCannedAcl(CannedAccessControlList.PublicRead);
                amazonS3Client.putObject(uploadFileObject);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } finally {
                if (tempFile != null) {
                    tempFile.delete();
                }
            }
        }).reduce((v1, v2) -> v1 && v2);
    }
}
