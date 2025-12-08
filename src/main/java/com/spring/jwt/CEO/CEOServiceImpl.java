package com.spring.jwt.CEO;

import com.spring.jwt.entity.Student;
import com.spring.jwt.Exam.entity.ExamResult;
import com.spring.jwt.exception.MonthlyChartCustomException;
import com.spring.jwt.exception.PieChartCustomException;
import com.spring.jwt.repository.StudentRepository;
import com.spring.jwt.Exam.repository.ExamResultRepository;
import com.spring.jwt.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Month;
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
            percentage = Math.floor(percentage * 100) / 100.0;
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

        List<DashboredDTO> averageStudents = students.stream()
                .filter(s -> studentPercentageMap.containsKey(s.getUserId()))
                .map(s -> new DashboredDTO(
                        s.getUserId(),
                        s.getName(),
                        s.getLastName(),
                        s.getStudentClass(),
                        s.getBatch(),
                        studentPercentageMap.get(s.getUserId())
                ))
                .filter(dto -> dto.getPercentage() >= 50 && dto.getPercentage() < 75)
                .sorted((d1, d2) -> Double.compare(d2.getPercentage(), d1.getPercentage()))
                .collect(Collectors.toList());

        if (averageStudents.isEmpty()) {
            throw new ResourceNotFoundException("No average students found for class " + studentClass + " and batch " + batch);
        }

        return averageStudents;
    }


    @Override
    public List<DashboredDTO> getBelowAverageStudents(String studentClass, String batch) {
        Map<String, Object> data = fetchData(studentClass, batch);
        List<Student> students = getUniqueStudents((List<Student>) data.get("students"));
        List<ExamResult> examResults = (List<ExamResult>) data.get("examResults");

        Map<Integer, Double> studentPercentageMap = calculateStudentPercentages(students, examResults);

        List<DashboredDTO> belowAverageStudents = students.stream()
                .filter(s -> {
                    Double perc = studentPercentageMap.get(s.getUserId());
                    return perc != null && perc < 50;
                })
                .map(s -> new DashboredDTO(
                        s.getUserId(),
                        s.getName(),
                        s.getLastName(),
                        s.getStudentClass(),
                        s.getBatch(),
                        studentPercentageMap.get(s.getUserId())
                ))
                .sorted((d1, d2) -> Double.compare(d2.getPercentage(), d1.getPercentage()))
                .collect(Collectors.toList());

        if (belowAverageStudents.isEmpty()) {
            throw new ResourceNotFoundException("No below average students found for class " + studentClass + " and batch " + batch);
        }

        return belowAverageStudents;
    }

    // -----------
    @Override
    public List<String> getStudentClass() {
        return studentRepository.findDistinctStudentClasses();
    }

    @Override
    public List<String> getStudentBatch() {
        return studentRepository.findByStudentBatch();
    }

    @Override
    public Map<String, Long> getPieChart() {

        try {
            List<Object[]> rows = studentRepository.countByClass();

            if (rows == null) {
                throw new PieChartCustomException("Database returned null for pie chart data");
            }

            if (rows.isEmpty()) {
                throw new PieChartCustomException("No student records found to generate pie chart");
            }

            Map<String, Long> pie = new HashMap<>();
            for (Object[] row : rows) {

                if (row[0] == null || row[1] == null) {
                    throw new PieChartCustomException("Invalid data found in pie chart query result");
                }
                String cls = String.valueOf(row[0]);
                Long count = ((Number) row[1]).longValue();
                pie.put("Students Class " + cls, count);
            }
            return pie;
        } catch (PieChartCustomException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new PieChartCustomException("Failed to fetch pie chart data: " + ex.getMessage());
        }
    }

    @Override
    public Map<String, Map<String, Integer>> getMonthlyChart(String studentClass, String batch) {

        try {
            Map<String, Map<String, Integer>> result = new LinkedHashMap<>();
            result.put("11", new LinkedHashMap<>());
            result.put("12", new LinkedHashMap<>());

            List<Object[]> rows = studentRepository.getMonthlyCountFiltered(studentClass, batch);

            if (rows == null) {
                throw new MonthlyChartCustomException("Database returned null for monthly chart data");
            }

            if (rows.isEmpty()) {
                throw new MonthlyChartCustomException("No attendance data available for selected filter");
            }

            for (Object[] row : rows) {

                if (row == null || row.length < 3) {
                    throw new MonthlyChartCustomException("Invalid data row returned from monthly chart query");
                }

                if (row[0] == null || row[1] == null || row[2] == null) {
                    throw new MonthlyChartCustomException("Null value found in monthly chart data");
                }

                Integer month = ((Number) row[0]).intValue();   // 1–12
                String cls = row[1].toString();                 // 11 / 12
                Integer pct = ((Number) row[2]).intValue();     // Percentage

                if (month < 1 || month > 12) {
                    throw new MonthlyChartCustomException("Invalid month number: " + month);
                }

                if (!result.containsKey(cls)) {
                    throw new MonthlyChartCustomException("Unexpected class found: " + cls);
                }

                String monthName = Month.of(month).name().substring(0, 3);
                result.get(cls).put(monthName, pct);
            }

            return result;

        } catch (MonthlyChartCustomException ex) {
            throw ex;  // rethrow custom exception
        } catch (Exception ex) {
            throw new MonthlyChartCustomException("Failed to fetch monthly chart data: " + ex.getMessage());
        }
    }



}
