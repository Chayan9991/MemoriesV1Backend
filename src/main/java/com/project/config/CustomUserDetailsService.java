package com.project.config;

import com.project.entity.admin.Admin;
import com.project.entity.user.User;
import com.project.repository.AdminRepository;
import com.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Check if the username exists as an Admin
        Optional<Admin> adminDetails = adminRepository.findByAdminEmail(username);
        if (adminDetails.isPresent()) {
            return adminDetails.get();
        }

        // If not found as Admin, check if it exists as a User
        Optional<User> userDetails = userRepository.findByUserEmail(username);
        if (userDetails.isPresent()) {
            return userDetails.get();
        }

        // If neither Admin nor User found, throw UsernameNotFoundException
        throw new UsernameNotFoundException("User not found with username: " + username);
    }

}
