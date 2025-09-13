package com.spring.jwt.Classes;
import com.spring.jwt.entity.Classes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.Mapping;

@Component
@RequiredArgsConstructor
public class ClassMapper {
    public ClassesDto toDto(Classes classes){
        if(classes == null){
            return null;
        }
        ClassesDto dto = new ClassesDto();
        dto.setClassesId(classes.getClassesId());
        dto.setSub(classes.getSub());
        dto.setStudentClass(classes.getStudentClass());
        dto.setDuration(classes.getDuration());
        dto.setDate(classes.getDate());
        dto.setTeacherId(classes.getTeacherId());
        dto.setTopic(classes.getTopic());
        dto.setTime(classes.getTime());
        dto.setTeacherName(classes.getTeacherName());
        return dto;
    }

    public Classes toEntity(ClassesDto dto){
        if(dto == null){
            return null;
        }
        Classes entity = new Classes();
        entity.setClassesId(dto.getClassesId());
        entity.setSub(dto.getSub());
        entity.setStudentClass(dto.getStudentClass());
        entity.setDuration(dto.getDuration());
        entity.setDate(dto.getDate());
        entity.setTeacherId(dto.getTeacherId());
        entity.setTopic(dto.getTopic());
        entity.setTime(dto.getTime());
        entity.setTeacherName(dto.getTeacherName());
        return entity;
    }

    public void updateEntityFromDto(ClassesDto dto, Classes existing) {
        if (dto == null) return;
        existing.setSub(dto.getSub());
        existing.setStudentClass(dto.getStudentClass());
        existing.setDuration(dto.getDuration());
        existing.setDate(dto.getDate());
        existing.setTeacherId(dto.getTeacherId());
        existing.setTopic(dto.getTopic());
        existing.setTime(dto.getTime());
        existing.setTeacherName(dto.getTeacherName());
    }
}
