package com.bookverse.service;


import com.bookverse.entity.Image;
import com.bookverse.repository.ImageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
@Service
@AllArgsConstructor
public class FileService {

    private final UserService userService;
    private final ImageRepository imageRepository;

    public void saveFile(String assetId, String url, String token) {
        Long userId = userService.getUserId(token);
        imageRepository.save(new Image(assetId, userId, url));
    }
}
