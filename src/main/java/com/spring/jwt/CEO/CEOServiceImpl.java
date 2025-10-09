package com.spring.jwt.CEO;

import com.spring.jwt.entity.Student;
import com.spring.jwt.Exam.entity.ExamResult;
import com.spring.jwt.repository.StudentRepository;
import com.spring.jwt.Exam.repository.ExamResultRepository;
import com.spring.jwt.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CEOServiceImpl implements CEOService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ExamResultRepository examResultRepository;

   
    public Map<Integer, Double> calculateStudentPercentages(List<Student> students, List<ExamResult> examResults) {
        Map<Integer, Double> studentPercentageMap = new HashMap<>();

        Set<Integer> uniqueUserIds = students.stream()
                .map(Student::getUserId)
                .collect(Collectors.toSet());

        for (Integer userId : uniqueUserIds) {
            List<ExamResult> resultsForStudent = examResults.stream()
                    .filter(er -> er.getUser().getId().equals(userId))
                    .toList();

            double totalScore = resultsForStudent.stream().mapToDouble(ExamResult::getScore).sum();
            double totalMarks = resultsForStudent.stream().mapToDouble(ExamResult::getTotalMarks).sum();

            double percentage = totalMarks > 0 ? (totalScore / totalMarks) * 100 : 0.0;

            if (totalMarks > 0) {
                studentPercentageMap.put(userId, percentage);
            }
        }

        return studentPercentageMap;
    }


    private List<Student> getUniqueStudents(List<Student> students) {
        List<Student> uniqueStudents = new ArrayList<>();
        Set<Integer> seenIds = new HashSet<>();

        for (Student s : students) {
            if (seenIds.add(s.getUserId())) {
                uniqueStudents.add(s);
            }
        }
        return uniqueStudents;
    }


    private Map<String, Object> fetchData(String studentClass, String batch) {
        if (studentClass == null || studentClass.isBlank()) {
            throw new IllegalArgumentException("Student class must be provided");
        }
        if (batch == null || batch.isBlank()) {
            throw new IllegalArgumentException("Batch must be provided");
        }

        List<Student> students = studentRepository.findByStudentClassAndBatch(studentClass, batch);
        if (students.isEmpty()) {
            throw new ResourceNotFoundException("No students found for class " + studentClass + " and batch " + batch);
        }

        List<Integer> userIds = students.stream().map(Student::getUserId).toList();
        List<ExamResult> examResults = examResultRepository.findByUser_IdIn(userIds);

        Map<String, Object> map = new HashMap<>();
        map.put("students", students);
        map.put("examResults", examResults);
        return map;
    }

    // ✅ 1. Batch Toppers (75%–100%)
    @Override
    public List<DashboredDTO> getBatchToppers(String studentClass, String batch) {
        Map<String, Object> data = fetchData(studentClass, batch);
        List<Student> students = getUniqueStudents((List<Student>) data.get("students"));
        List<ExamResult> examResults = (List<ExamResult>) data.get("examResults");

        Map<Integer, Double> studentPercentageMap = calculateStudentPercentages(students, examResults);

        List<DashboredDTO> toppers = new ArrayList<>();
        for (Student s : students) {
            Double perc = studentPercentageMap.get(s.getUserId());
            if (perc != null && perc >= 75 && perc <= 100) {
                toppers.add(new DashboredDTO(
                        s.getUserId(),
                        s.getName(),
                        s.getLastName(),
                        s.getStudentClass(),
                        s.getBatch(),
                        perc
                ));
            }
        }


        if (toppers.isEmpty()) {
            throw new ResourceNotFoundException("No toppers found for class " + studentClass + " and batch " + batch);
        }

        toppers.sort((d1, d2) -> Double.compare(d2.getPercentage(), d1.getPercentage()));
        return toppers;
    }


    @Override
    public List<DashboredDTO> getAverageStudents(String studentClass, String batch) {
        Map<String, Object> data = fetchData(studentClass, batch);
        List<Student> students = getUniqueStudents((List<Student>) data.get("students"));
        List<ExamResult> examResults = (List<ExamResult>) data.get("examResults");

        Map<Integer, Double> studentPercentageMap = calculateStudentPercentages(students, examResults);

        List<DashboredDTO> averageStudents = new ArrayList<>();
        for (Student s : students) {
            Double perc = studentPercentageMap.get(s.getUserId());
            if (perc != null && perc >= 50 && perc < 75) {
                averageStudents.add(new DashboredDTO(
                        s.getUserId(),
                        s.getName(),
                        s.getLastName(),
                        s.getStudentClass(),
                        s.getBatch(),
                        perc
                ));
            }
        }


        if (averageStudents.isEmpty()) {
            throw new ResourceNotFoundException("No average students found for class " + studentClass + " and batch " + batch);
        }

        averageStudents.sort((d1, d2) -> Double.compare(d2.getPercentage(), d1.getPercentage()));
        return averageStudents;
    }


    @Override
    public List<DashboredDTO> getBelowAverageStudents(String studentClass, String batch) {
        Map<String, Object> data = fetchData(studentClass, batch);
        List<Student> students = getUniqueStudents((List<Student>) data.get("students"));
        List<ExamResult> examResults = (List<ExamResult>) data.get("examResults");

        Map<Integer, Double> studentPercentageMap = calculateStudentPercentages(students, examResults);

        List<DashboredDTO> belowAverageStudents = new ArrayList<>();
        for (Student s : students) {
            Double perc = studentPercentageMap.get(s.getUserId());
            if (perc != null && perc < 50) {
                belowAverageStudents.add(new DashboredDTO(
                        s.getUserId(),
                        s.getName(),
                        s.getLastName(),
                        s.getStudentClass(),
                        s.getBatch(),
                        perc
                ));
            }
        }


        if (belowAverageStudents.isEmpty()) {
            throw new ResourceNotFoundException("No below average students found for class " + studentClass + " and batch " + batch);
        }

        belowAverageStudents.sort((d1, d2) -> Double.compare(d2.getPercentage(), d1.getPercentage()));
        return belowAverageStudents;
    }
}
