package com.spring.jwt.service.impl;

import com.spring.jwt.dto.CeoProfileResponseDto;
import com.spring.jwt.dto.CeoProfileUpdateDto;
import com.spring.jwt.entity.User;
import com.spring.jwt.exception.UserNotFoundExceptions;
import com.spring.jwt.repository.CeoProfileRepository;
import com.spring.jwt.service.CeoProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CeoProfileServiceImpl implements CeoProfileService {

    private final CeoProfileRepository ceoProfileRepository;

    @Override
    public CeoProfileResponseDto getProfile(Integer userId) {
        try {
            User user = ceoProfileRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundExceptions("User not found"));

            CeoProfileResponseDto dto = new CeoProfileResponseDto();
            dto.setFullName(user.getFirstName() + " " + user.getLastName());
            dto.setUserName(user.getEmail());
            dto.setMobileNumber(user.getMobileNumber());

            return dto;

        } catch (UserNotFoundExceptions ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException("Failed to fetch profile", ex);
        }
    }


    @Override
    public void updateProfile(Integer userId, CeoProfileUpdateDto dto) {
        try {
            User user = ceoProfileRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundExceptions("User not found"));

            if (dto.getFullName() != null && !dto.getFullName().isBlank()) {

                String[] nameParts = dto.getFullName().trim().split("\\s+", 2);

                user.setFirstName(nameParts[0]);

                if (nameParts.length > 1) {
                    user.setLastName(nameParts[1]);
                }
            }
            if (dto.getUserName() != null) {
                user.setEmail(dto.getUserName());
            }

            if (dto.getMobileNumber() != null) {
                user.setMobileNumber(Long.parseLong(dto.getMobileNumber()));
            }

            ceoProfileRepository.save(user);

        } catch (UserNotFoundExceptions ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException("Failed to update profile", ex);
        }
    }
}
