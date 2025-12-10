package com.bookverse.controller;

import com.bookverse.enums.ErrorCode;
import com.bookverse.exception.AppException;
import com.bookverse.service.FileService;
import com.bookverse.service.UploadService;
import com.bookverse.service.UserService;
import com.bookverse.utils.ParamKey;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/upload")
@AllArgsConstructor
public class UploadController {

    private final UploadService uploadService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, String> upload(
            @RequestPart("file") MultipartFile file
    ) {

        if (file == null || file.isEmpty()) {
            throw new AppException(ErrorCode.INVALID_INPUT);
        }
        return uploadService.uploadImage(file);
    }
}

