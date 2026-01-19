package com.bookverse.controller;

import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/tts")
@AllArgsConstructor
public class TtsController {

    private final RestClient tts;

    @PostMapping(value = "", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> tts(@RequestBody SpeakReq req) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("text", req.text());

        byte[] wav = tts.post()
                .uri("/tts")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.APPLICATION_OCTET_STREAM)
                .body(body)
                .retrieve()
                .body(byte[].class);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("audio/wav"))
                .cacheControl(CacheControl.noStore())
                .body(new ByteArrayResource(wav));
    }

    @PostMapping(value = "/clone", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> clone(
            @RequestPart("text") String text,
            @RequestPart("refText") String refText,
            @RequestPart("refAudio") MultipartFile refAudio
    ) throws Exception {

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("text", text);
        body.add("ref_text", refText);

        ByteArrayResource fileRes = new ByteArrayResource(refAudio.getBytes()) {
            @Override public String getFilename() {
                return (refAudio.getOriginalFilename() == null) ? "ref.wav" : refAudio.getOriginalFilename();
            }
        };

        HttpHeaders fileHeaders = new HttpHeaders();
        fileHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        body.add("ref_audio", new HttpEntity<>(fileRes, fileHeaders));

        byte[] wav = tts.post()
                .uri("/clone")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.APPLICATION_OCTET_STREAM)
                .body(body)
                .retrieve()
                .body(byte[].class);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("audio/wav"))
                .cacheControl(CacheControl.noStore())
                .body(new ByteArrayResource(wav));
    }

    public record SpeakReq(String text) {}
}
