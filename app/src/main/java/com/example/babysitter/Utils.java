package com.example.babysitter;

public class Utils {
    public static String generateImageUrl(String imageName) {
        return "https://firebasestorage.googleapis.com/v0/b/babycare-d1be6.appspot.com/o/"
                + imageName
                + "?alt=media";
    }
}
