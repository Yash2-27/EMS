package com.spring.jwt.service;

import com.spring.jwt.dto.CeoProfileResponseDto;
import com.spring.jwt.dto.CeoProfileUpdateDto;

public interface CeoProfileService {

    CeoProfileResponseDto getProfile(Integer userId);

    void updateProfile(Integer userId, CeoProfileUpdateDto dto);
}
