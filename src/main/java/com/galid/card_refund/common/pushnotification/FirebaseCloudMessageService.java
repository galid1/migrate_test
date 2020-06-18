package com.galid.card_refund.common.pushnotification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.galid.card_refund.domains.user.domain.PushTokenEntity;
import com.galid.card_refund.domains.user.domain.PushTokenRepository;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.apache.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FirebaseCloudMessageService {
    private final String API_URL = "https://fcm.googleapis.com/v1/projects/tourcash-13092/messages:send";
    private final ObjectMapper objectMapper;
    private final PushTokenRepository pushTokenRepository;

    public void sendMessageTo(PushNotificationEvent event) {
        String message = makeMessage(event.getUserId(),
                                     event.getTitle(),
                                     event.getBody());

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .build();

        try {
            Response response = client.newCall(request)
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getAccessToken() {
        String FIREBASE_KEY_FILE = "/.refund/firebase_service_key.json";

        GoogleCredentials googleCredentials = null;
        try {
            googleCredentials = GoogleCredentials
                    .fromStream(new FileInputStream(System.getProperty("user.home") + FIREBASE_KEY_FILE))
                    .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

            googleCredentials.refreshIfExpired();
        } catch(IOException e) {
            System.out.println("Firebase로 부터 AccessToken을 얻는 도중 에러 발생.");
            e.printStackTrace();
        }

        return googleCredentials.getAccessToken().getTokenValue();
    }

    private String makeMessage(Long userId, String title, String body) {
        PushTokenEntity pushTokenEntity = pushTokenRepository.findFirstByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저에게 알림을 보내기 위한 PushToken이 존재하지 않거나, PushToken이 잘못되었습니다."));

        FcmMessage fcmMessage = FcmMessage.builder()
                .message(FcmMessage.Message.builder()
                    .token(pushTokenEntity.getPushToken())
                    .notification(FcmMessage.Notification.builder()
                        .title(title)
                        .body(body)
                        .image(null)
                        .build()
                    )
                    .build()
                )
                .validate_only(false)
                .build();

        String fcmMessageString = "";

        try {
            fcmMessageString = objectMapper.writeValueAsString(fcmMessage);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return fcmMessageString;
    }

}
