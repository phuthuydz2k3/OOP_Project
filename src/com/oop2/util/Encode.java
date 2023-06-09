package com.oop2.util;

import java.text.*;
import java.util.regex.Pattern;


// Encode Vietnamese
public class Encode {
    public static String encodeString(String input) {
        // Normalize the text
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);

        // Remove diacritical marks
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String withoutDiacritics = pattern.matcher(normalized).replaceAll("");

        // Convert to lowercase and replace non-alphanumeric characters and spaces with hyphen
        String lowercase = withoutDiacritics.toLowerCase();
        String encoded = lowercase.replaceAll("[^a-zA-Z0-9\\s]", "").replaceAll("\\s", "-");

        return encoded;
    }

    // testing
    public static void main(String[] args) {
        String input = "Chủ tịch Hồ Chí Minh";
        String encoded = encodeString(input);
        System.out.println(encoded);  // Output: le-hoan
    }

}
