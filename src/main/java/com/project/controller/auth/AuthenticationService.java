package com.project.controller.auth;

import com.project.entity.Roles;
import com.project.entity.admin.Admin;
import com.project.entity.user.User;
import com.project.entity.user.UserDiary;
import com.project.jwt.JwtService;
import com.project.repository.AdminRepository;
import com.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AdminRepository adminRepository;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {

        // Check if user with the given email already exists
        Optional<User> existingUser = userRepository.findByUserEmail(request.getEmail());
        if (existingUser.isPresent()) {
            throw new RuntimeException("User with this email is already registered");
        }

        if (request.getRole() == Roles.USER) {
            var user = User.builder()
                    .userFullName(request.getFullName())
                    .userEmail(request.getEmail())
                    .userPassword(passwordEncoder.encode(request.getPassword()))
                    .role(Roles.USER)
                    .build();

            //Create the User Diary
            UserDiary userDiary = new UserDiary();
            userDiary.setUser(user);
            user.setDiary(userDiary);

            userRepository.save(user);
            var jwtToken = jwtService.generateToken(user);
            return AuthResponse.builder()
                    .token(jwtToken).build();

        }else{
            // If the user is ADMIN        //Anyone can become Admin now (It needs to be fixed)
            var admin = Admin.builder()
                    .adminName(request.getFullName())
                    .adminEmail(request.getEmail())
                    .adminPassword(passwordEncoder.encode(request.getPassword()))
                    .role(Roles.ADMIN)
                    .build();

            adminRepository.save(admin);
            var jwtToken = jwtService.generateToken(admin);
            return AuthResponse.builder()
                    .token(jwtToken).build();

        }

    }
    public AuthResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            Optional<User> user = userRepository.findByUserEmail(request.getEmail());
            if (user.isPresent()) {
                var jwtToken = jwtService.generateToken(user.get());
                return AuthResponse.builder()
                        .token(jwtToken).build();
            }

            Optional<Admin> admin = adminRepository.findByAdminEmail(request.getEmail());
            if (admin.isPresent()) {

                var jwtToken = jwtService.generateToken(admin.get());
                return AuthResponse.builder()
                        .token(jwtToken).build();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        // If neither user nor admin found, authentication fails
        return AuthResponse.builder().token("User Not Authenticated!!!").build();
    }

}
