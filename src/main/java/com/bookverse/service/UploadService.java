package com.bookverse.service;

import com.bookverse.utils.MultipartHttpServletRequest;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Part;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;


@Service
@SuppressWarnings("unchecked")
@AllArgsConstructor
@Slf4j
public class UploadService {

    private final Cloudinary cloudinary;

    public Map<String, Object> callExternalAPI(MultipartHttpServletRequest request) throws ServletException, IOException {
        Part filePart = request.getPart("file");
        Map<String, Object> response = new HashMap<String, Object>();
        try (InputStream inputStream = filePart.getInputStream()) {
            byte[] bytes = inputStream.readAllBytes();
            response = cloudinary.uploader().upload(
                    bytes,
                    ObjectUtils.emptyMap()
            );

        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
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
