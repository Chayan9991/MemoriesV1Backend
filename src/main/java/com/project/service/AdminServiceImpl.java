package com.project.service;

import com.project.entity.admin.Admin;
import com.project.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService{

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public Admin createAdmin(Admin admin) {

            Admin savedAdmin = adminRepository.save(admin);

            if(savedAdmin == null){
                throw new RuntimeException("Something Went wrong while saving the Admin");
            }
            return savedAdmin;

    }
}
