package com.secondExample.demo.Service;

import com.secondExample.demo.Models.Pixel;
import com.secondExample.demo.Models.Role;
import com.secondExample.demo.Models.User;

import javax.crypto.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;

public class Utils {

    public static boolean isAdmin(String encryptedToken, SecretKey secretKey){
        String decryptedToken = decrypt(encryptedToken, secretKey);
        String[] parts = decryptedToken.split("_");
        String role = parts[2];

        return role.equals(Role.ADMIN.toString());
    }

    public static String userRole(String encryptedToken, SecretKey secretKey){
        String decryptedToken = decrypt(encryptedToken, secretKey);
        String[] parts = decryptedToken.split("_");
        String role = parts[2];

        return role;
    }

    public static User getUserByName(String username,List<User> users) {
        for (User user : users) {
            if (user.name.equals(username)) {
                return user;
            }
        }
        return null;
    }

    public static String userName(String encryptedToken, SecretKey secretKey){
        String decryptedToken = decrypt(encryptedToken, secretKey);
        String[] parts = decryptedToken.split("_");
        String role = parts[1];

        return role;
    }

    public static String encrypt(String token, SecretKey secretKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] encryptedBytes = cipher.doFinal(token.getBytes());

        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String encryptedToken, SecretKey secretKey )  {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedToken));

            String decryptedString = new String(decryptedBytes);

            return decryptedString;
        } catch (Exception e) {
            return "";
        }

    }

    public static boolean isTokenValid(String token, SecretKey secretKey,long tokenLifetimeInMillis) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        String decryptedToken = Utils.decrypt(token , secretKey);
        if(decryptedToken.isEmpty()){
            return false;
        }
        LocalDateTime dateOfTokenСreation = Utils.extractDateTimeFromToken(decryptedToken);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tokenExpiryTime = dateOfTokenСreation.plus(Duration.ofMillis(tokenLifetimeInMillis));

        if (now.isAfter(tokenExpiryTime)) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isValidPassword(String password) { // не менее 3 цифр, 3 заглавных букв, проверка на пробелы, разрешённые символы: англ, цифры, спец.символы
        if (password.length() < 8) {
            return false;
        }
        int digitCount = 0;
        int uppercaseCount = 0;
        boolean specialChar = false;

        for (char c : password.toCharArray()) {
            if (Character.isWhitespace(c)) {
                return false;
            } else if (Character.isDigit(c)) {
                digitCount++;
            } else if (Character.isUpperCase(c)) {
                if (c < 'A' || c > 'Z') {
                    return false;
                }
                uppercaseCount++;
            } else if (!Character.isLetterOrDigit(c)) {
                specialChar = true;
            } else if (Character.isLowerCase(c)) {
                if (c < 'a' || c > 'z') {
                    return false;
                }
            }
        }
        if (digitCount < 3 || uppercaseCount < 3 || !specialChar) {
            return false;
        }
        return true;
    }

    public static boolean arePixelsOverlapping(Pixel p1, Pixel p2) {
        int width = 16;
        int height = 16;

        boolean left = p1.x + width <= p2.x; //p1 слева от p2
        boolean right = p2.x + width <= p1.x; //p1 справа от p2
        boolean above = p1.y + height <= p2.y; //p1 выше p2
        boolean below = p2.y + height <= p1.y; //p1 ниже p2

        boolean isOverlapping = !(left || right || above || below);
        return isOverlapping;
    }

    public static boolean deletePixelFromList(Pixel pixel, List<Pixel> pixels) {
        for (Pixel existingPixel : pixels) {
            if (existingPixel.x == pixel.x && existingPixel.y == pixel.y) {
                pixels.remove(existingPixel);
                return true;
            }
        }
        return false;
    }

    public static LocalDateTime extractDateTimeFromToken(String token) {
        String[] parts = token.split("_");

        String day = parts[3];
        String month = parts[4];
        String year = parts[5];
        String hour = parts[6];
        String minute = parts[7];
        String second = parts[8];

        String dateTimeString = String.format("%s_%s_%s_%s_%s_%s", day, month, year, hour, minute, second);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm_ss");
        return LocalDateTime.parse(dateTimeString, formatter);
    }
}
