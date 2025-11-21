package com.bookverse.service;

import com.bookverse.enums.ErrorCode;
import com.bookverse.exception.AppException;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.Map;


@Service
@AllArgsConstructor
@Slf4j
@SuppressWarnings("unchecked")
public class UploadService {

    private final Cloudinary cloudinary;

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

    public static String sign(long timestamp, String apiSecret) {
        try {
            String data = "timestamp=" + timestamp + apiSecret;
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] bytes = md.digest(data.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();

        } catch (Exception e) {
            throw new RuntimeException("Error generating Cloudinary signature", e);
        }
    }
}
