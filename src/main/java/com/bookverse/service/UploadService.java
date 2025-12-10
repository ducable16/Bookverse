package com.bookverse.service;

import com.bookverse.enums.ErrorCode;
import com.bookverse.exception.AppException;
import com.bookverse.utils.ParamKey;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;


@Service
@AllArgsConstructor
@Slf4j
@SuppressWarnings("unchecked")
public class UploadService {

    private final Cloudinary cloudinary;
    private final FileService fileService;

    public Map<String, Object> uploadToCloudinary(MultipartFile file) {

        try {
            byte[] bytes = file.getBytes();

            return cloudinary.uploader().upload(
                    bytes,
                    ObjectUtils.emptyMap()
            );

        } catch (IOException e) {
            log.error("Upload to Cloudinary failed", e);
            throw new AppException(ErrorCode.UPLOAD_FAILED);
        }
    }

    public Map<String, String> uploadImage(MultipartFile file) {
        Map<String, Object> fileDetail = uploadToCloudinary(file);
        System.out.println("Cloudinary upload result: {}" + fileDetail);

        String assetId = fileDetail.get(ParamKey.ASSET_ID).toString();
        String url = fileDetail.get(ParamKey.URL).toString();
        System.out.println("Uploaded file URL: {}" + url);
        System.out.println("Uploaded file assetID: {}" + assetId);

        fileService.saveFile(assetId, url);

        Map<String, String> result = new HashMap<>();
        result.put(ParamKey.URL, url);
        return result;
    }
}
