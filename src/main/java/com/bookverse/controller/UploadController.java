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
    private final FileService fileService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Object upload(
            @RequestPart("file") MultipartFile file
    ) {

        if (file == null || file.isEmpty()) {
            throw new AppException(ErrorCode.INVALID_INPUT);
        }

        Map<String, Object> fileDetail = uploadService.uploadToCloudinary(file);
        System.out.println("Cloudinary upload result: {}" + fileDetail);

        String assetId = fileDetail.get(ParamKey.ASSET_ID).toString();
        String url = fileDetail.get(ParamKey.URL).toString();

        fileService.saveFile(assetId, url);

        JSONObject result = new JSONObject();
        result.put(ParamKey.URL, url);

        return result;
    }
}

