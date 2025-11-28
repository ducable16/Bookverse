package com.bookverse.service;

import com.bookverse.dto.request.SendOTPRequest;
import com.bookverse.dto.request.VerifyOTPRequest;
import com.bookverse.dto.response.TokenResponse;
import com.bookverse.dto.user.UserDto;
import com.bookverse.entity.OTPCode;
import com.bookverse.entity.User;
import com.bookverse.enums.ErrorCode;
import com.bookverse.exception.AppException;
import com.bookverse.exception.EntityNotFoundException;
import com.bookverse.repository.UserRepository;
import com.bookverse.utils.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@AllArgsConstructor
public class OtpService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final MailService mailService;
    private final Map<String, OTPCode> otpSet = new HashMap<>();


    public TokenResponse verifyOTP(VerifyOTPRequest request) {
        User user = userRepository.findByEmail(request.getEmail().toLowerCase()).orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));
        if (!otpSet.containsKey(request.getEmail())) {
            throw new AppException(ErrorCode.OTP_INCORRECT);
        }
        if (otpSet.get(request.getEmail()).getExpireTime() > System.currentTimeMillis()) {
            throw new AppException(ErrorCode.OTP_EXPIRED);
        }
        UserDto dto = UserDto.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
        String token = jwtService.generateToken(dto);
        return new TokenResponse(token);
    }

    public void sendOTP(SendOTPRequest request) {
        User user = userRepository.findByEmail(request.getEmail().toLowerCase()).orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));
        Random random = new Random();
        OTPCode otpCode = new OTPCode();
        String newOtp = String.valueOf(random.nextInt(100000));
        while (newOtp.length() < 6) {
            newOtp = "0" + newOtp;
        }
        otpCode.setCode(newOtp);
        otpCode.setExpireTime((int)System.currentTimeMillis() / 1000 + 60);
        otpSet.put(request.getEmail(), otpCode);
        mailService.sendEmail(request.getEmail(), "OTP CODE", "Your otp code is: " + newOtp);
    }

    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void cleanupOTPSet() {
        long now = System.currentTimeMillis();
        otpSet.entrySet().removeIf(entry -> entry.getValue().getExpireTime() < now);
    }
}
