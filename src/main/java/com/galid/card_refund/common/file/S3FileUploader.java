package com.galid.card_refund.common.file;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.UUID;

@Component
public class S3FileUploader {
    private String BUCKET_NAME = "card-refund";

    private AmazonS3 s3Client;

    public S3FileUploader() {
        this.s3Client = AmazonS3Client.builder()
                .standard()
                .withRegion(Regions.AP_NORTHEAST_2)
                .build();
    }

    public void uploadFile(String key, byte[] byteArray) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(MediaType.IMAGE_PNG_VALUE);
        objectMetadata.setContentLength(byteArray.length);

        PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME,
                createObjectPath(key),
                new ByteArrayInputStream(byteArray),
                objectMetadata).withCannedAcl(CannedAccessControlList.PublicRead);

        s3Client.putObject(putObjectRequest);
    }

    private String createObjectPath(String key) {
        int year = LocalDate.now().getYear();
        int month = LocalDate.now().getMonthValue();
        int day = LocalDate.now().getDayOfMonth();

        return key + "/"
                + year + "/"
                + month + "/"
                + day + "/"
                + UUID.randomUUID() + ".png";
    }

}
