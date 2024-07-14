package com.project.image;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.project.entity.admin.Admin;
import com.project.entity.user.User;
import com.project.repository.AdminRepository;
import com.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Service

public class ImageUploadService {

    @Autowired
    private  Cloudinary cloudinary ;
    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private  AdminRepository adminRepository;

    public String uploadImage(MultipartFile file, String folderName, String imageName) throws IOException{
        Map uploadParams = ObjectUtils.asMap(
                "folder", folderName,
                "public_id", imageName
        );
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), uploadParams);
        String imageUrl = uploadResult.get("url").toString();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();

        Optional<User> userOpt = userRepository.findByUserEmail(currentUserName);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setUserProfileImage(imageUrl);
            userRepository.save(user);
        } else {
            Optional<Admin> adminOpt = adminRepository.findByAdminEmail(currentUserName);
            if (adminOpt.isPresent()) {
                Admin admin = adminOpt.get();
                admin.setAdminProfileImage(imageUrl);
                adminRepository.save(admin);
            }
        }

        return imageUrl;
    }


    public void deleteImage(String imageUrl) throws IOException {
        // Extract the public ID from the image URL
        String publicId = extractPublicIdFromUrl(imageUrl);
        String imageFolder = "memoriesV1/";

        // Delete the image
        cloudinary.uploader().destroy(imageFolder+publicId, ObjectUtils.emptyMap());
    }

    private String extractPublicIdFromUrl(String imageUrl) {
        String[] parts = imageUrl.split("/");
        String publicIdWithExtension = parts[parts.length - 1];
        return publicIdWithExtension.split("\\.")[0]; //publicId means the name of Image
    }
}
