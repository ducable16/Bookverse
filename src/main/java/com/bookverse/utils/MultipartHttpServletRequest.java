package com.bookverse.utils;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.Part;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class MultipartHttpServletRequest extends HttpServletRequestWrapper {

    private final Map<String, MultipartFile> multipartFiles = new LinkedHashMap<>();
    private final Map<String, String[]> multipartParams = new LinkedHashMap<>();

    public MultipartHttpServletRequest(HttpServletRequest request) throws IOException {
        super(request);
        parseRequest(request);
    }

    private void parseRequest(HttpServletRequest request) {
        try {
            Collection<Part> parts = request.getParts();
            for (Part part : parts) {
                String fileName = part.getSubmittedFileName();

                if (fileName != null) {
                    MultipartFile file = new MultipartFile(part);
                    multipartFiles.put(part.getName(), file);
                } else {
                    try (BufferedReader reader = new BufferedReader(
                            new InputStreamReader(part.getInputStream(), StandardCharsets.UTF_8))) {
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            sb.append(line);
                        }
                        multipartParams.put(part.getName(), new String[]{ sb.toString() });
                    }
                }
            }
        } catch (IOException | ServletException e) {
            e.printStackTrace();
        }
    }

    public MultipartFile getFile(String name) {
        return multipartFiles.get(name);
    }

    public Map<String, MultipartFile> getFileMap() {
        return Collections.unmodifiableMap(multipartFiles);
    }

    @Override
    public String getParameter(String name) {
        String[] values = multipartParams.get(name);
        if (values != null && values.length > 0) {
            return values[0];
        }
        return super.getParameter(name);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> combined = new HashMap<>(super.getParameterMap());
        combined.putAll(multipartParams);
        return Collections.unmodifiableMap(combined);
    }

}
