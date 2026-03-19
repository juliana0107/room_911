package com.lavoratorioxyz.room_911.controller;

import com.lavoratorioxyz.room_911.entity.CompanyProfile;
import com.lavoratorioxyz.room_911.repository.CompanyProfileRepository;
import com.lavoratorioxyz.room_911.service.FileStorageService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/company")
@CrossOrigin(origins = "*")
public class CompanyProfileController {

    private final CompanyProfileRepository repository;
    private final FileStorageService fileStorageService;

    public CompanyProfileController(CompanyProfileRepository repository,
                                    FileStorageService fileStorageService) {
        this.repository = repository;
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/profile")
    public CompanyProfile saveProfile(@RequestBody CompanyProfile profile) {
        return repository.save(profile);
    }

    @GetMapping("/profile")
    public CompanyProfile getProfile() {
        return repository.findAll().stream().findFirst().orElse(null);    }

    @PostMapping("/upload-logo")
    public CompanyProfile uploadLogo(@RequestParam("file") MultipartFile file) {

        String fileName = fileStorageService.storeFile(file);

        String fileUrl = "/uploads/logos/" + fileName;

        CompanyProfile profile = getProfile();

        if (profile == null) {
            profile = new CompanyProfile();
        }

        profile.setLogoUrl(fileUrl);

        return repository.save(profile);
    }
}