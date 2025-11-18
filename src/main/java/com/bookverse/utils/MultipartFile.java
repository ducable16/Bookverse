package com.bookverse.utils;

import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class MultipartFile {

    private final Part part;

    public MultipartFile(Part part) {
        this.part = part;
    }

    public String getName() {
        return part.getName();
    }

    public String getOriginalFilename() {
        String filename = part.getSubmittedFileName();
        return filename != null ? filename : "";
    }

    public String getContentType() {
        return part.getContentType();
    }

    public boolean isEmpty() {
        return part.getSize() == 0;
    }

    public long getSize() {
        return part.getSize();
    }

    public InputStream getInputStream() throws IOException {
        return part.getInputStream();
    }

    public byte[] getBytes() throws IOException {
        try (InputStream in = getInputStream()) {
            return in.readAllBytes();
        }
    }

    public void transferTo(File dest) throws IOException {
        part.write(dest.getAbsolutePath());
    }

}
