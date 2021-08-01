package com.indialone.mongodbdemo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class MongodbdemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MongodbdemoApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(StudentRepository repository, MongoTemplate mongoTemplate) {
        return args -> {
            Address address = new Address(
                    "India",
                    "Visnager",
                    "384315"
            );

            String emailUru = "urvashiprajapati1707@gmail.com";
            String emailNatu = "prajapatinatavar21197@gmail.com";

            Student studentUru = new Student(
                    "Urvashi",
                    "Prajapati",
                    emailUru,
                    Gender.FEMALE,
                    address,
                    Arrays.asList("Maths", "Science", "English"),
                    BigDecimal.TEN,
                    LocalDateTime.now()
            );

            Student studentNatu = new Student(
                    "Natavar",
                    "Prajapati",
                    emailNatu,
                    Gender.MALE,
                    address,
                    Arrays.asList("Maths", "Science", "English"),
                    BigDecimal.TEN,
                    LocalDateTime.now()
            );

//            usingMongoTemplateAndQuery(repository, mongoTemplate, email, studentUru, studentNatu);

            repository.findStudentByEmail(emailNatu)
                    .ifPresentOrElse(s -> {
                        System.out.println(studentUru + " is already exists");
                    }, () -> {
                        System.out.println("Inserting student " + studentUru);
                        repository.insert(studentNatu);
                    });


        };
    }

    private void usingMongoTemplateAndQuery(StudentRepository repository, MongoTemplate mongoTemplate, String email, Student studentUru, Student studentNatu) {
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(email));

        List<Student> students = mongoTemplate.find(query, Student.class);

        if (students.size() > 1) {
            throw new IllegalStateException("found many student with email" + email);
        }

        if (students.isEmpty()) {
            System.out.println("Inserting the student" + studentUru);
//                repository.insert(studentUru);
            repository.insert(studentNatu);
        } else {
            System.out.println(studentUru + "already a student");
        }
    }

}
