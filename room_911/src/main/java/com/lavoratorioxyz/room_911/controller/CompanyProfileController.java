package com.lavoratorioxyz.room_911.controller;

import com.lavoratorioxyz.room_911.entity.CompanyProfile;
import com.lavoratorioxyz.room_911.repository.CompanyProfileRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/company")
@CrossOrigin(origins = "*")
public class CompanyProfileController {

    private final CompanyProfileRepository repository;

    public CompanyProfileController(CompanyProfileRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/profile")
    public CompanyProfile saveProfile(@RequestBody CompanyProfile profile) {
        return repository.save(profile);
    }

    @GetMapping("/profile")
    public CompanyProfile getProfile() {
        return repository.findAll().stream().findFirst().orElse(null);    }

    @PostMapping("/upload-logo")
    public String uploadLogo(@RequestParam("file") MultipartFile file) {
        return "pendiente"; // luego lo hacemos bien
    }
}