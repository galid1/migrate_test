package com.galid.card_refund.config;

import com.galid.card_refund.common.aws.S3FileUploader;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@Component
public class S3FileUploaderSetUp {
    @MockBean
    private S3FileUploader s3FileUploader;

    @PostConstruct
    public void init() {
        given(s3FileUploader.uploadFile(any(), any(), any()))
                .willReturn("TEST");
    }
}
