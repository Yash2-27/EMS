package com.spring.jwt.ExamResult;

import com.spring.jwt.exception.ExamResultNotFoundException;
import com.spring.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeeExamResultServiceImpl implements SeeExamResultService {

    private final SeeExamResultRepository examResultRepository;
    private final UserRepository userRepository;

    @Override
    public SeeExamResultDTO getStudentExamResult(Long userId, String subject, String topic) {

        if (!userRepository.existsById(userId)) {
            throw new ExamResultNotFoundException("Exam result not found for userId: " + userId);
        }

        List<Object[]> resultList = examResultRepository.findStudentExamResult(userId, subject, topic);

        if (resultList.isEmpty()) {

            if (subject != null && examResultRepository.countBySubject(subject) == 0) {
                throw new ExamResultNotFoundException("Exam result not found for subject: " + subject);
            }

            if (topic != null && examResultRepository.countByTopic(topic) == 0) {
                throw new ExamResultNotFoundException("Exam result not found for topic: " + topic);
            }

        }

        Object[] row = resultList.get(0);

        return new SeeExamResultDTO(
                row[0] != null ? row[0].toString() : null,                // subject
                row[1] != null ? row[1].toString() : null,                // topic
                row[2] != null ? row[2].toString() : null,                // examDate
                row[3] != null ? ((Number) row[3]).intValue() : null,     // noOfQuestions
                row[5] != null ? ((Number) row[5]).intValue() : null,     // answered
                row[6] != null ? ((Number) row[6]).intValue() : null,     // notAnswered
                row[9] != null ? ((Number) row[9]).intValue() : null,     // overallRank
                row[8] != null ? ((Number) row[8]).intValue() : null,     // totalStudents
                row[7] != null ? ((Number) row[7]).intValue() : null,     // studentMarks
                row[4] != null ? ((Number) row[4]).intValue() : null      // totalMarks
        );
    }
}
