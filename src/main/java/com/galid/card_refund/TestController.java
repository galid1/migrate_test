package com.galid.card_refund;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Controller
public class TestController {

    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @PostMapping("/test")
    public void testPost(@RequestBody FilePost post) throws IOException {
        System.out.println(post.file);

        byte[] arr = Base64.decodeBase64(post.getFile().getBytes());
        File file = new File("/Users/jeonjun-yeob/image.png");
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(arr);

        file.createNewFile();
    }

    @Data
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    private static class FilePost {
        private String file;

        public FilePost(String file) {
            this.file = file;
        }
    }
}
