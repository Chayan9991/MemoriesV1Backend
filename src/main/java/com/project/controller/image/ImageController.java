package com.project.controller.image;

import com.project.image.ImageUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final ImageUploadService imageUploadService;

    @Autowired
    public ImageController(ImageUploadService imageUploadService) {
        this.imageUploadService = imageUploadService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String folderName = "memoriesV1";

            // Generate a filename with timestamp
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String imageName = dateFormat.format(new Date());

            String imageUrl = imageUploadService.uploadImage(file, folderName,imageName);
            return ResponseEntity.ok().body("Image uploaded successfully. URL: " + imageUrl);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload image: " + e.getMessage());
        }
    }


    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteImage(@RequestParam("url") String imageUrl) {
        try {
            imageUploadService.deleteImage(imageUrl);

            return new ResponseEntity<>("Image deleted successfully", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to delete image", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}