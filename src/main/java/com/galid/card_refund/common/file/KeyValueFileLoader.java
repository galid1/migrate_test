package com.galid.card_refund.common.file;

import org.springframework.stereotype.Component;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class KeyValueFileLoader {

    private String filePathStartWithUserHome = System.getProperty("user.home") + "/";

    private Map<String, Map<String, Optional<String>>> storeMaps = new HashMap<>();

    public String getValueFromFile(String filePathMapKey, String rowKey) {
        if (this.storeMaps.get(filePathMapKey) == null) {
            initValueMap(filePathMapKey);
        }

        return this.storeMaps.get(filePathMapKey)
                .get(rowKey)
                .orElseThrow(() -> new IllegalArgumentException("키 값에 해당하는 값이 KeyValueStore에 존재하지 않습니다."));
    }

    private void initValueMap(String filePath) {
        String resultPath = filePathStartWithUserHome + filePath;
        Map<String, Optional<String>> keyValueStore = new HashMap<>();

        File file = new File(resultPath);
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));

            String line = "";
            String[] keyValuePair = null;

            while((line = br.readLine()) != null) {
                keyValuePair = line.split("=");
                keyValueStore.put(keyValuePair[0], Optional.of(keyValuePair[1]));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.storeMaps.put(filePath, keyValueStore);
    }
}
