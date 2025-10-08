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


    //Calculating Batch Topper

    @Override
    public List<DashboredDTO> getBatchToppers(String studentClass, String batch) {

try {
    if (studentClass == null || studentClass.isBlank()) {
        throw new IllegalArgumentException("Student class must be provided");
    }
    if (batch == null || batch.isBlank()) {
        throw new IllegalArgumentException("Batch must be provided");
    }
    } catch (ResourceNotFoundException e) {
    throw new ResourceNotFoundException("Student class not found");
    }
    catch (Exception e) {
    throw e;

    }

            List<Student> students = studentRepository.findByStudentClassAndBatch(studentClass, batch);

           try {
               if (students.isEmpty()) {
                   throw new ResourceNotFoundException("No students found for class " + studentClass + " and batch " + batch);
               }

           }catch (ResourceNotFoundException e) {
               throw new ResourceNotFoundException("Student class not found");
           }
            List<Integer> userIds = new ArrayList<>();
            for (Student s : students) {
                userIds.add(s.getUserId());
            }

        List<ExamResult> examResults = examResultRepository.findByUser_IdIn(userIds);

        Map<Integer, Double> studentPercentageMap = calculateStudentPercentages(students, examResults);

        List<Student> uniqueStudents = new ArrayList<>();
        Set<Integer> seenIds = new HashSet<>();
        for (Student s : students) {
            if (!seenIds.contains(s.getUserId())) {
                uniqueStudents.add(s);
                seenIds.add(s.getUserId());
            }
        }

        List<DashboredDTO> toppers = new ArrayList<>();
        for (Student s : uniqueStudents) {
            Double perc = studentPercentageMap.get(s.getUserId());
            if (perc != null && perc >= 75 && perc <= 100) {
                DashboredDTO dto = new DashboredDTO(
                        s.getUserId(),
                        s.getName(),
                        s.getLastName(),
                        s.getStudentClass(),
                        s.getBatch(),
                        perc
                );
                toppers.add(dto);
            }
            if (toppers.isEmpty()) {
                throw new ResourceNotFoundException("No toppers found for class " + studentClass + " and batch " + batch);
            }

        }

        Collections.sort(toppers, new Comparator<DashboredDTO>() {
            @Override
            public int compare(DashboredDTO d1, DashboredDTO d2) {
                if (d2.getPercentage() > d1.getPercentage()) {
                    return 1;
                } else if (d2.getPercentage() < d1.getPercentage()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });


        return toppers;
    }



    // Calculating Average Student

    @Override
    public List<DashboredDTO> getAverageStudents(String studentClass, String batch) {
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

        List<Integer> userIds = new ArrayList<>();
        for (Student s : students) {
            userIds.add(s.getUserId());
        }

        List<ExamResult> examResults = examResultRepository.findByUser_IdIn(userIds);
        Map<Integer, Double> studentPercentageMap = calculateStudentPercentages(students, examResults);

        List<Student> uniqueStudents = new ArrayList<>();
        Set<Integer> seenIds = new HashSet<>();
        for (Student s : students) {
            if (!seenIds.contains(s.getUserId())) {
                uniqueStudents.add(s);
                seenIds.add(s.getUserId());
            }
        }

        List<DashboredDTO> averageStudents = new ArrayList<>();
        for (Student s : uniqueStudents) {
            Double perc = studentPercentageMap.get(s.getUserId());
            if (perc != null && perc >= 50 && perc < 75) {
                DashboredDTO dto = new DashboredDTO(
                        s.getUserId(),
                        s.getName(),
                        s.getLastName(),
                        s.getStudentClass(),
                        s.getBatch(),
                        perc
                );
                averageStudents.add(dto);
            }
            if(averageStudents.isEmpty()){
                throw new ResourceNotFoundException("No average students found for class " + studentClass + " and batch " + batch);


            }
        }

        Collections.sort(averageStudents, new Comparator<DashboredDTO>() {
            @Override
            public int compare(DashboredDTO d1, DashboredDTO d2) {
                if (d2.getPercentage() > d1.getPercentage()) {
                    return 1;
                } else if (d2.getPercentage() < d1.getPercentage()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });

        return averageStudents;
    }


    // Calculating Below Average Student

    @Override
    public List<DashboredDTO> getBelowAverageStudents(String studentClass, String batch) {
        if (studentClass == null || studentClass.isBlank()) {
            throw new IllegalArgumentException("Student class must be provided");
        }
        if (batch == null || batch.isBlank()) {
            throw new IllegalArgumentException("Batch must be provided");
        }

        List<Student> students = studentRepository.findByStudentClassAndBatch(studentClass, batch);
        try {
            if (students.isEmpty()) {
                throw new ResourceNotFoundException("No students found for class " + studentClass + " and batch " + batch);
            }
        }catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Student class not found");
        }

        List<Integer> userIds = new ArrayList<>();
        for (Student s : students) {
            userIds.add(s.getUserId());
        }

        List<ExamResult> examResults = examResultRepository.findByUser_IdIn(userIds);
        Map<Integer, Double> studentPercentageMap = calculateStudentPercentages(students, examResults);

        List<Student> uniqueStudents = new ArrayList<>();
        Set<Integer> seenIds = new HashSet<>();
        for (Student s : students) {
            if (!seenIds.contains(s.getUserId())) {
                uniqueStudents.add(s);
                seenIds.add(s.getUserId());
            }
        }

        List<DashboredDTO> belowAverageStudents = new ArrayList<>();
        for (Student s : uniqueStudents) {
            Double perc = studentPercentageMap.get(s.getUserId());
            if (perc != null && perc < 50) {
                DashboredDTO dto = new DashboredDTO(
                        s.getUserId(),
                        s.getName(),
                        s.getLastName(),
                        s.getStudentClass(),
                        s.getBatch(),
                        perc
                );
                belowAverageStudents.add(dto);
            }
            if(belowAverageStudents.isEmpty()){
                throw new ResourceNotFoundException("No Below Average student found for "+studentClass+ "and Batch "+batch);
            }

        }

        Collections.sort(belowAverageStudents, new Comparator<DashboredDTO>() {
            @Override
            public int compare(DashboredDTO d1, DashboredDTO d2) {
                if (d2.getPercentage() > d1.getPercentage()) {
                    return 1;
                } else if (d2.getPercentage() < d1.getPercentage()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });

        return belowAverageStudents;
    }

}
