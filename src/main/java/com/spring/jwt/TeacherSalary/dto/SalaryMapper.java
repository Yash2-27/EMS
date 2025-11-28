package com.spring.jwt.TeacherSalary.dto;

import com.spring.jwt.TeacherSalary.entity.TeacherSalaryMonthly;
import com.spring.jwt.TeacherSalary.entity.TeacherSalaryStructure;
import com.spring.jwt.dto.TeacherDTO;
import com.spring.jwt.entity.Teacher;

import java.time.format.DateTimeFormatter;

public class SalaryMapper {

    public static SalaryStructureResponseDto toStructureResponse(TeacherSalaryStructure s) {
        SalaryStructureResponseDto dto = new SalaryStructureResponseDto();
        dto.setStructureId(s.getStructureId());
        dto.setTeacherId(s.getTeacherId());
        dto.setPerDaySalary(s.getPerDaySalary());
        dto.setStatus(s.getStatus());
        dto.setCreatedAt(s.getCreatedAt());
        dto.setUpdatedAt(s.getUpdatedAt());
        dto.setAnnualSalary(s.getAnnualSalary());
        dto.setTeacherName(s.getTeacherName());
        return dto;
    }

    public static SalaryResponseDto toSalaryResponse(TeacherSalaryMonthly m) {
        SalaryResponseDto dto = new SalaryResponseDto();
        dto.setSalaryId(m.getSalaryId());
        dto.setTeacherId(m.getTeacherId());
        dto.setMonth(m.getMonth());
        dto.setYear(m.getYear());
        dto.setCalculatedSalary(m.getCalculatedSalary());
        dto.setDeduction(m.getDeduction());
        dto.setFinalSalary(m.getFinalSalary());
        dto.setStatus(m.getStatus());
        dto.setAbsentDays(m.getAbsentDays());
        dto.setPresentDays(m.getPresentDays());
        dto.setHalfDays(m.getHalfDays());
        dto.setLateDays(m.getLateDays());
        dto.setPerDaySalary(m.getPerDaySalary());
        dto.setTotalDays(m.getTotalDays());

        dto.setPaymentDate(
                m.getPaymentDate() == null
                        ? null
                        : m.getPaymentDate().format(DateTimeFormatter.ISO_DATE_TIME)
        );
        return dto;
    }

    // --------------------------------------------------
    // Mapper for partial update request -> response
    // --------------------------------------------------
    public static SalaryStructureResponseDto toStructureResponse(
            TeacherSalaryStructure s, SalaryStructureUpdateRequestDto req) {

        SalaryStructureResponseDto dto = new SalaryStructureResponseDto();
        dto.setStructureId(s.getStructureId());
        dto.setTeacherId(s.getTeacherId());
        dto.setTeacherName(s.getTeacherName());
        dto.setStatus(s.getStatus());
        dto.setCreatedAt(s.getCreatedAt());
        dto.setUpdatedAt(s.getUpdatedAt());

        // Use updated values if provided in request, otherwise keep existing
        dto.setPerDaySalary(req.getPerDaySalary() != null ? req.getPerDaySalary() : s.getPerDaySalary());
        dto.setAnnualSalary(req.getAnnualSalary() != null ? req.getAnnualSalary() : s.getAnnualSalary());

        return dto;
    }
}
