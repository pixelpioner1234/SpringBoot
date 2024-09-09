package com.secondExample.demo.Controllers;

import com.secondExample.demo.Models.*;
import com.secondExample.demo.Service.Utils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
// 1.сокеты 2.запушить пиксели в репозиторий 3.решить архитектуру
@RestController
@RequestMapping("/simple")
public class SimpleTokenController {

    // пусть у каждого пискеля будет свой автор +
    // пусть будет 2 роли пользователей: обычный и админ +
    // метод для получения роли из токена +

    public long tokenLifetimeInMillis = 300000;
    private final List<String> tokens = new ArrayList<>();
    private final List<User> users = new ArrayList<>();
    public  List<Pixel> pixels = new ArrayList<>();
    KeyGenerator keyGen = KeyGenerator.getInstance("AES");
    SecretKey secretKey = keyGen.generateKey();
    public SimpleTokenController() throws NoSuchAlgorithmException{}


    @PostMapping("/register")
    public String register(@RequestBody User user) {
        for(User current : users){
            if(current.name.equals(user.name)){
                return "This name is taken.";
            }
        }

        if (!Utils.isValidPassword(user.pass)) {
            return "Password must be at least 8 characters long, include a digit, an uppercase letter, and a special character.";
        }

        if(user.role == null){
            user.role= Role.REGULAR;
        }
        if (user.role != Role.ADMIN && user.role != Role.REGULAR) {
            user.role = Role.REGULAR;
        }

        users.add(user);
        return "User added.";
    }

    @GetMapping("/allUsers")
    public List<User> getAllUsers(){
        return users;
    }


    @PostMapping("/login")
    public String login(@RequestBody User user) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {

        User userFromList = null;

        for(User current : users){
            if(current.name.equals(user.name)){
                userFromList = current;
            }
        }

        if(userFromList == null){
            return "User has not found.";
        }

        if(!userFromList.pass.equals(user.pass)){
            return "Wrong password.";
        }

        String prefix = "Token";
        LocalDateTime creationTime = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm_ss");
        String dateInfo = creationTime.format(formatter);

        String token = prefix + "_" + user.name + "_" + user.role.toString() + "_" + dateInfo;
        return Utils.encrypt(token, secretKey);
    }


    @PostMapping("/setPixel")
    public String createPixel(@RequestBody Pixel pixel , @RequestHeader("myToken") String token) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        if(!Utils.isTokenValid(token, secretKey, tokenLifetimeInMillis)){
            return "Token is invalid.";
        }
        if(pixel.x > 496 || pixel.y > 496){
            return "The coordinates don't fit";
        }
        for (Pixel existingPixel : pixels) {
            if (existingPixel.x == pixel.x && existingPixel.y == pixel.y) {
                return "A pixel already exists at these coordinates.";
            }
        }

        for (Pixel existingPixel : pixels) {
            if (Utils.arePixelsOverlapping(existingPixel, pixel)) {
                return "This pixel overlaps with an existing one.";
            }
        }

        String userName = Utils.userName(token,secretKey);
        pixel.author = Utils.getUserByName(userName,users);

        pixels.add(pixel);
        return  "Pixel is created.";
    }


    @PostMapping("/deletePixel")
    public String deletePixel(@RequestBody Pixel pixel,@RequestHeader("Token") String token ) throws InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException {
        if(!Utils.isTokenValid(token, secretKey, tokenLifetimeInMillis)){
            return "Token is invalid.";
        }

        String userName = Utils.userName(token,secretKey);
        User user = Utils.getUserByName(userName,users);
        if(!user.name.equals(pixel.author.name)){
            return "You can't delete the pixels of others";
        }

        if (Utils.deletePixelFromList(pixel,pixels)) {
            return "Pixel is deleted.";
        } else {
            return "Pixel not found.";
        }
    }


    @GetMapping("/deleteAllPixels")
    public String deleteAllPixels(@RequestHeader("Token") String token ) throws InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException {
        if(!Utils.isTokenValid(token, secretKey, tokenLifetimeInMillis)){
            return "Token is invalid.";
        }
        if(Utils.isAdmin(token,secretKey)){
            pixels.clear();
            return "Pixels are removed.";
        }
        return "Sorry, you aren't admin.";
    }


    @GetMapping("/getAllTokens")
    public List<String> getAllTokens() {
        return tokens;
    }


    @GetMapping("/showPixels")
    public ResponseEntity<byte[]> showPixels() {
        try {
            BufferedImage blackImage = new BufferedImage(512, 512, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = blackImage.createGraphics();
            graphics.setColor(Color.BLACK);
            graphics.fillRect(0, 0, 512, 512);
            for(Pixel pixel:pixels){
                graphics.setColor(pixel.getCustomColor());
                graphics.fillRect(pixel.x,pixel.y, pixel.width, pixel.height);
            }
            graphics.dispose();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(blackImage, "png", baos);
            byte[] imageBytes = baos.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "image/png");
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
