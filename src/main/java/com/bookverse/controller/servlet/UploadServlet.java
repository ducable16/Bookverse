package com.bookverse.controller.servlet;

import com.bookverse.enums.ErrorCode;
import com.bookverse.service.FileService;
import com.bookverse.service.UploadService;
import com.bookverse.utils.MultipartFile;
import com.bookverse.utils.MultipartHttpServletRequest;
import com.bookverse.utils.ParamKey;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.PrintWriter;
import java.time.OffsetDateTime;
import java.util.Map;

@WebServlet("/upload")
@MultipartConfig
public class UploadServlet extends HttpServlet {

    @Autowired
    private UploadService uploadService;

    @Autowired
    private FileService fileService;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("text/plain; charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_OK);
            JSONObject obj = new JSONObject();

            String token = request.getHeader("Authorization");
            if (token == null || token.isEmpty()) {
                obj.put(ParamKey.CODE, ErrorCode.UNAUTHORIZED);
                obj.put(ParamKey.DATA, JSONObject.NULL);
                obj.put(ParamKey.MESSAGE, ErrorCode.UNAUTHORIZED.getMessage());
                try (PrintWriter out = response.getWriter()) {
                    out.write(obj.toString());
                }
                return;
            }

            String contentType = request.getContentType();
            if (contentType == null || !contentType.toLowerCase().startsWith("multipart/")) {
                obj.put(ParamKey.CODE, ErrorCode.INVALID_INPUT);
                obj.put(ParamKey.DATA, JSONObject.NULL);
                obj.put(ParamKey.MESSAGE, ErrorCode.INVALID_INPUT.getMessage());
                try (PrintWriter out = response.getWriter()) {
                    out.write(obj.toString());
                }
                return;
            }

            MultipartHttpServletRequest multipartRequest = new MultipartHttpServletRequest(request);

            MultipartFile file = multipartRequest.getFile("file");
            if (file == null) {
                obj.put(ParamKey.CODE, ErrorCode.INVALID_INPUT);
                obj.put(ParamKey.DATA, JSONObject.NULL);
                obj.put(ParamKey.DATA, ErrorCode.INVALID_INPUT.getMessage());
                return;
            }
            Map<String, Object> fileDetail = uploadService.callExternalAPI(multipartRequest);
            String assetId = fileDetail.get(ParamKey.ASSET_ID).toString();
            String url = fileDetail.get(ParamKey.URL).toString();
//            OffsetDateTime createdAt = OffsetDateTime.parse(fileDetail.get(ParamKey.CREATED_AT).toString());
            fileService.saveFile(assetId, url, token);
            obj.put(ParamKey.CODE, ErrorCode.SUCCESS);
            obj.put(ParamKey.MESSAGE, ErrorCode.SUCCESS.getMessage());
            JSONObject result = new JSONObject();
            result.put(ParamKey.URL, url);
            obj.put(ParamKey.DATA, result);
            try (PrintWriter out = response.getWriter()) {
                out.write(obj.toString());
            }

        } catch (Exception e) {
            response.setStatus(ErrorCode.INVALID_INPUT.getCode());
        }
    }
}
