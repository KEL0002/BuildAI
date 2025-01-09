package de.kel0002.buildai.util;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Dictionary;
import java.util.Enumeration;


public class RequestHandler {
    public static String dorequest(String url_string, Dictionary<String, Object> payload) {
        try {

            String jsonPayload = createJsonPayloadFromDictionary(payload);

            URL url = new URL(url_string);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            InputStream responseStream = responseCode == 200
                    ? connection.getInputStream()
                    : connection.getErrorStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream, StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line.trim());
            }
            reader.close();

            return response.toString();
        } catch (Exception e) {
            return null;
        }
    }

    private static String createJsonPayloadFromDictionary(Dictionary<String, Object> payload) {
        StringBuilder jsonBuilder = new StringBuilder("{");
        Enumeration<String> keys = payload.keys();

        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            Object value = payload.get(key);

            if (value instanceof Boolean) {
                jsonBuilder.append("\"").append(key).append("\":").append(value);
            } else if (value instanceof String) {
                jsonBuilder.append("\"").append(key).append("\":\"").append(value).append("\"");
            }

            if (keys.hasMoreElements()) {
                jsonBuilder.append(",");
            }
        }

        jsonBuilder.append("}");
        return jsonBuilder.toString();
    }

}
