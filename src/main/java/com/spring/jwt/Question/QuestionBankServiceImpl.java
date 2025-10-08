package com.spring.jwt.Question;

import com.spring.jwt.entity.Question;
import com.spring.jwt.exception.ResourceNotFoundException;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionBankServiceImpl implements QuestionBankService {

    private final QuestionBankRepository repository;
    private final QuestionRepository questionRepository;

    @Autowired
    public QuestionBankServiceImpl(QuestionBankRepository repository, QuestionRepository questionRepository) {
        this.repository = repository;
        this.questionRepository = questionRepository;
    }



    @Override
    public List<QuestionBankDTO> getTeachersByStudentClass(String studentClass) {

        if (studentClass == null || studentClass.isBlank()) {
            throw new IllegalArgumentException("Student class must not be null or empty");
        }

        try {

            List<QuestionBankDTO> teachers = repository.findTeachersByStudentClass(studentClass);


            if (teachers == null || teachers.isEmpty()) {
                throw new ResourceNotFoundException(
                        "No teachers found for student class: '" + studentClass + "'"
                );
            }


            return teachers;

        } catch (ResourceNotFoundException e) {

            throw e;
        } catch (Exception e) {

            throw new ServiceException(
                    "Unexpected error occurred while fetching teachers for student class: " + studentClass, e
            );
        }
    }


    @Override
    public List<QuestionBankSubjectDropdown> getTopicsBySubjectAndStudentClass(String subject, String studentClass) {

      try{ if (subject == null || subject.isBlank()) {

          throw new IllegalArgumentException("Subject must not be null or empty");
      }
          if (studentClass == null || studentClass.isBlank()) {
              throw new IllegalArgumentException("Student class must not be null or empty");
          }

          List<Question> questions = questionRepository.findBySubjectAndStudentClass(subject, studentClass);

          if (questions.isEmpty()) {

              throw new ResourceNotFoundException(
                      "No topics found for subject: '" + subject + "' and student class: '" + studentClass + "'"
              );
          }

          return questions.stream()
                  .map(q -> new QuestionBankSubjectDropdown( q.getTopic()))
                  .distinct()
                  .collect(Collectors.toList());

      }
      catch (ResourceNotFoundException e) {
          throw e;
      }

      catch (Exception e){

          throw new ServiceException(
                  "Unexpected error occurred while fetching teachers for student class: " + studentClass, e
          );

      }



    }


    @Override
    public List<QuestionBankQuestionsDTO> getFilteredQuestions(String studentClass, String name,  String subject,String topic)
    {

        try{
            if (name==null || name.isBlank()) {
                throw new ResourceNotFoundException("Name must not be null or empty");
            }
            if (subject==null || subject.isBlank()) {
                throw new ResourceNotFoundException("Subject must not be null or empty");
            }
            if (topic==null || topic.trim().isBlank()) {
                throw new ResourceNotFoundException("Topic must not be null or empty");
            }
            if (studentClass==null || studentClass.isBlank()) {
                throw new ResourceNotFoundException("Student class must not be null or empty");
            }

            List<QuestionBankQuestionsDTO> QuestionSummery = questionRepository.findFilteredQuestions(studentClass,name,subject,topic);

            if (QuestionSummery.isEmpty()) {
                throw new ResourceNotFoundException("No Qustion Bank Questions found With Student Class = " +studentClass+ " , Teacher Name =" +name+" , Subject = "  +subject+", Topic = "+topic);
            }

        }
        catch (ResourceNotFoundException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ServiceException(
                    "Unexpected error occurred while fetching teachers for student class: " + studentClass, e
            );
        }


        return questionRepository.findFilteredQuestions(studentClass, name, subject, topic);
    }








}
