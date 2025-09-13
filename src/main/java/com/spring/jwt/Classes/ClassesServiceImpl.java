package com.spring.jwt.Classes;
import com.spring.jwt.entity.Classes;
import com.spring.jwt.entity.Teacher;
import com.spring.jwt.exception.TeacherNotFoundException;
import com.spring.jwt.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClassesServiceImpl implements ClassesService {

    @Autowired
    private ClassesRepository classesRepository;

    @Autowired
    private ClassMapper mapper;

    @Autowired
    private TeacherRepository teacherRepository;

    @Override
    public ClassesDto createClass(ClassesDto classesDto) {
        if (classesDto == null) {
            throw new IllegalArgumentException("Class data cannot be null");
        }

        // Validation: teacher should not have two classes at the same time
        if (classesRepository.existsByTeacherIdAndDateAndTime(
                classesDto.getTeacherId(), classesDto.getDate(), classesDto.getTime())) {
            throw new IllegalArgumentException("This teacher is already scheduled for another class at this time.");
        }

        // Fetch teacher from DB to get the correct name
        Teacher teacher = teacherRepository.findById(classesDto.getTeacherId())
                .orElseThrow(() -> new TeacherNotFoundException("Teacher not found"));

        // Map DTO to entity
        Classes entity = mapper.toEntity(classesDto);
        entity.setTeacherId(teacher.getTeacherId());
        entity.setTeacherName(teacher.getName()); //  set teacherName from DB

        Classes savedClass = classesRepository.save(entity);

        ClassesDto responseDto = mapper.toDto(savedClass);
        responseDto.setTeacherName(teacher.getName()); // ensure response has correct name
        return responseDto;
    }

    @Override
    public List<ClassesDto> getClassBySubject(String sub, String studentClass) {
        List<ClassesDto> classes = classesRepository.findClassBySubject(sub.toLowerCase(), studentClass)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        if (classes.isEmpty()) {
            throw new ClassesNotFoundException("Class not found");
        }
        return classes;
    }

    @Override
    public ClassesDto updateClass(Long id, ClassesDto classesDto) {
        Classes existing = classesRepository.findById(id)
                .orElseThrow(() -> new ClassesNotFoundException("Class with id : " + id + " not found"));

        // Validation: teacher schedule conflict
        if (classesDto.getTeacherId() != null && classesDto.getDate() != null && classesDto.getTime() != null) {
            if (classesRepository.existsByTeacherIdAndDateAndTime(
                    classesDto.getTeacherId(), classesDto.getDate(), classesDto.getTime())) {
                throw new IllegalArgumentException("Teacher is already scheduled for another class at this time.");
            }
        }
        // Fetch teacher from DB
        Teacher teacher = teacherRepository.findById(classesDto.getTeacherId())
                .orElseThrow(() -> new TeacherNotFoundException("Teacher not found"));
        // Update entity using mapper
        mapper.updateEntityFromDto(classesDto, existing);

        // Enforce teacher info from DB
        existing.setTeacherId(teacher.getTeacherId());
        existing.setTeacherName(teacher.getName());
        Classes saved = classesRepository.save(existing);
        ClassesDto responseDto = mapper.toDto(saved);
        responseDto.setTeacherName(teacher.getName()); // ensure correct name in response
        return responseDto;
    }
    @Override
    public void deleteClass(Long id) {
        Classes classes = classesRepository.findById(id)
                .orElseThrow(() -> new ClassesNotFoundException("Class does not exist"));
        classesRepository.delete(classes);
    }
    @Override
    public List<ClassesDto> getTodaysClassesByStudentClass(String studentClass) {
        LocalDate today = LocalDate.now();
        return classesRepository.findTodaysClassesByStudentClass(studentClass, today)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ClassesDto> getUpcomingClasses(String studentClass) {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        return classesRepository.findUpcomingClassesByStudentClass(studentClass, today, now)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}
/**Use .orElseThrow(...) when working with Optional (like findById).
Use throw new ... with condition checks (like conflict validations).
That’s why in Teacher not found we use .orElseThrow(...), but in schedule conflict we use throw new
**/




//package com.spring.jwt.Classes;
//
//import com.spring.jwt.entity.Classes;
//import com.spring.jwt.entity.Teacher;
//import com.spring.jwt.exception.TeacherNotFoundException;
//import com.spring.jwt.repository.TeacherRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class ClassesServiceImpl implements ClassesService {
//
//    @Autowired
//    private ClassesRepository classesRepository;
//
//    @Autowired
//    private ClassMapper mapper;
//
//    @Autowired
//    private TeacherRepository teacherRepository; // Added repository
//
//    @Override
//    public ClassesDto createClass(ClassesDto classesDto) {
//        if (classesDto == null) {
//            throw new IllegalArgumentException("Class data cannot be null");
//        }
//        //  Validation: teacher should not have two classes at the same time
//        if (classesRepository.existsByTeacherIdAndDateAndTime(
//                classesDto.getTeacherId(), classesDto.getDate(), classesDto.getTime())) {
//            throw new IllegalArgumentException("This teacher is already scheduled for another class at this time.");
//        }
//        //  Always fetch teacher from DB to ensure correct teacher name
//        Teacher teacher = teacherRepository.findById(classesDto.getTeacherId())
//                .orElseThrow(() -> new TeacherNotFoundException("Teacher not found"));
//        Classes entity = mapper.toEntity(classesDto);
//        entity.setTeacherId(teacher.getTeacherId());
//        Classes savedClass = classesRepository.save(entity);
//        return mapper.toDto(savedClass);
//    }
//    @Override
//    public List<ClassesDto> getClassBySubject(String sub, String studentClass) {
//        List<ClassesDto> classes = classesRepository.findClassBySubject(
//                        sub.toLowerCase(), studentClass)
//                .stream()
//                .map(mapper::toDto)
//                .collect(Collectors.toList());
//        if (classes.isEmpty()) {
//            throw new ClassesNotFoundException("Class not found");
//        }
//        return classes;
//    }
//    @Override
//    public ClassesDto updateClass(Long id, ClassesDto classesDto) {
//        Classes existing = classesRepository.findById(id)
//                .orElseThrow(() -> new ClassesNotFoundException("Class with id : " + id + " not found"));
//        //  Validation (teacher schedule conflict)
//        if (classesDto.getTeacherId() != null && classesDto.getDate() != null && classesDto.getTime() != null) {
//            if (classesRepository.existsByTeacherIdAndDateAndTime(
//                    classesDto.getTeacherId(), classesDto.getDate(), classesDto.getTime())) {
//                throw new IllegalArgumentException("Teacher is already scheduled for another class at this time.");
//            }
//        }
//        //  Always fetch teacher from DB
//        Teacher teacher = teacherRepository.findById(classesDto.getTeacherId())
//                .orElseThrow(() -> new TeacherNotFoundException("Teacher not found"));
//        // Update fields from DTO but enforce teacher data from DB
//        existing.setSub(classesDto.getSub());
//        existing.setDate(classesDto.getDate());
//        existing.setDuration(classesDto.getDuration());
//        existing.setStudentClass(classesDto.getStudentClass());
//        existing.setTeacherId(teacher.getTeacherId());
//        existing.setTopic(classesDto.getTopic());
//        existing.setTime(classesDto.getTime());
//        existing.setTeacherName(classesDto.getTeacherName());
//
//        Classes saved = classesRepository.save(existing);
//        return mapper.toDto(saved);
//    }
//        @Override
//        public void deleteClass(Long id) {
//        Classes classes = classesRepository.findById(id)
//                .orElseThrow(() -> new ClassesNotFoundException("Class does not exist"));
//        classesRepository.delete(classes);
//    }
//        //  getTodaysClasses by studentClass
//        public List<ClassesDto> getTodaysClassesByStudentClass(String studentClass) {
//        LocalDate today = LocalDate.now();
//        return classesRepository.findTodaysClassesByStudentClass(studentClass, today)
//                .stream()
//                .map(mapper::toDto)
//                .collect(Collectors.toList());
//    }
//         //  getUpcomingClasses by studentClass
//        public List<ClassesDto> getUpcomingClasses(String studentClass) {
//        LocalDate today = LocalDate.now();
//        LocalTime now = LocalTime.now();
//        return classesRepository.findUpcomingClassesByStudentClass(studentClass, today, now)
//                .stream()
//                .map(mapper::toDto)
//                .collect(Collectors.toList());
//    }
//}
