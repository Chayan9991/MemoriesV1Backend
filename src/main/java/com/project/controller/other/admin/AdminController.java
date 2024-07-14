package com.project.controller.other.admin;

import com.project.entity.admin.Admin;
import com.project.entity.user.User;
import com.project.repository.AdminRepository;
import com.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;

    @GetMapping()
    public ResponseEntity<String> adminDemo() {
        return ResponseEntity.ok("Hello Admin...");
    }
    //Admin Management
    //To add new admin from authenticated Admin panel.. Current admin can generate a new code
    // the code need to entered in the form which new admin is trying to fill.. then new admin will be added

   @PutMapping("/updateAdminDetails/{id}")
   public ResponseEntity<?> updateAdminDetails(@PathVariable UUID id, @RequestBody Admin admin){
        Optional<Admin> dbAdmin = adminRepository.findById(id);
        if(dbAdmin.isPresent()){
            dbAdmin.get().setAdminName(admin.getAdminName());
            dbAdmin.get().setAdminProfileImage(admin.getAdminProfileImage());
            adminRepository.save(dbAdmin.get());
            return ResponseEntity.ok(dbAdmin);
       }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

   }


    @GetMapping("/getAllAdmin")
    public ResponseEntity<Map<String, Object>> fetchAdminList() {
        List<Admin> list = adminRepository.findAll();
        if (!list.isEmpty()) {
            Map<String ,Object> response = new HashMap<>();
            response.put("size", list.size());
            response.put("users", list);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


    //User Management
    @GetMapping("/getAllUsers")
    public ResponseEntity<Map<String, Object>> fetchUserList() {
        List<User> list = userRepository.findAll();
        if (!list.isEmpty()) {
            Map<String ,Object> response = new HashMap<>();
            response.put("size", list.size());
            response.put("users", list);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/getUserById/{id}")
    public ResponseEntity<User> fetchUserById(@PathVariable UUID id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID id) {
        Optional<User> user =  userRepository.findById(id);
        if(user.isPresent()){
            userRepository.delete(user.get());
            return ResponseEntity.ok("User Deleted Successfully...");
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build() ;
        }

    }


}
