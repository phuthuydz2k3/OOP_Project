package com.oop.util;

public class UrlDecode {
    public static String getCodeFromUrl(String url) {
        String[] cs = url.split("/");
        return cs[cs.length - 1];
    }
}
