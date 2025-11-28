package com.bookverse.controller;

import com.bookverse.enums.ErrorCode;
import com.bookverse.service.FileService;
import com.bookverse.service.UploadService;
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
@RequestMapping("/upload")
@AllArgsConstructor
public class UploadController {

    private final UploadService uploadService;
    private final FileService fileService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> upload(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestPart("file") MultipartFile file
    ) {

        if (token == null || token.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(error(ErrorCode.UNAUTHORIZED));
        }

        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(error(ErrorCode.INVALID_INPUT));
        }

        Map<String, Object> fileDetail = uploadService.uploadToCloudinary(file);

        String assetId = fileDetail.get(ParamKey.ASSET_ID).toString();
        String url = fileDetail.get(ParamKey.URL).toString();

        fileService.saveFile(assetId, url, token);

        JSONObject result = new JSONObject();
        result.put(ParamKey.URL, url);

        return ResponseEntity.ok(success(result));
    }

    private JSONObject success(Object data) {
        JSONObject obj = new JSONObject();
        obj.put(ParamKey.CODE, ErrorCode.SUCCESS);
        obj.put(ParamKey.MESSAGE, ErrorCode.SUCCESS.getMessage());
        obj.put(ParamKey.DATA, data);
        return obj;
    }

    private JSONObject error(ErrorCode errorCode) {
        JSONObject obj = new JSONObject();
        obj.put(ParamKey.CODE, errorCode);
        obj.put(ParamKey.MESSAGE, errorCode.getMessage());
        obj.put(ParamKey.DATA, JSONObject.NULL);
        return obj;
    }
}

