package de.kel0002.buildai.util;

import java.util.ArrayList;


public class ResponseFormatter {
    public static String extractResponseField(String jsonResponse) {
        try {
            int responseIndex = jsonResponse.indexOf("\"response\":");
            if (responseIndex == -1) {
                return null;
            }

            int start = jsonResponse.indexOf("\"", responseIndex + 11) + 1;
            int end = jsonResponse.indexOf("\"", start);

            if (start == -1 || end == -1) {
                return null;
            }

            return jsonResponse.substring(start, end);
        } catch (Exception e) {
            return null;
        }
    }


    public static ArrayList<String> extractResponseLines(String responseString) {
        String[] lines = responseString.split("[/\\n\\r]+");
        ArrayList<String> linesList = new ArrayList<>();

        for (String line : lines) {
            line = line.trim();

            if (line.isEmpty()) {
                continue;
            }

            if (line.startsWith("/")) {
                line = line.substring(1).trim();
            }

            while (line.endsWith("\\n") || !endsWithAny(line, getLettersAndNumbers())) {
                if (line.endsWith("\\n")) {
                    line = line.substring(0, line.length() - 2).trim();
                } else {
                    line = line.substring(0, line.length() - 1).trim();
                }
            }
            linesList.add(line);
        }

        return linesList;
    }


    public static ArrayList<String> getLettersAndNumbers() {
        ArrayList<String> list = new ArrayList<>();

        for (char c = 'a'; c <= 'z'; c++) {
            list.add(String.valueOf(c));
        }

        for (char c = 'A'; c <= 'Z'; c++) {
            list.add(String.valueOf(c));
        }

        for (char c = '0'; c <= '9'; c++) {
            list.add(String.valueOf(c));
        }

        return list;
    }


    public static boolean endsWithAny(String line, ArrayList<String> characters) {
        for (String character : characters) {
            if (line.endsWith(character)) {
                return true;
            }
        }
        return false;
    }
}
