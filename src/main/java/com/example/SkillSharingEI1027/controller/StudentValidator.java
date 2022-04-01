package com.example.SkillSharingEI1027.controller;

import com.example.SkillSharingEI1027.modelo.Student;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Arrays;
import java.util.List;

public class StudentValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Student.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        Student student = (Student) obj;
        if (student.getName().trim().equals(""))
            errors.rejectValue("name","compulsory","Name must be introduced");
        if (student.getPassword().trim().equals(""))
            errors.rejectValue("password","compulsory","Password must be introduced");
        List<String> degreeValues = Arrays.asList("Ingeniería Informática","Diseño y Desarrollo de Videojuegos","Ingeniería Eléctrica","Arquitectura Técnica","Ingeniería en Diseño Industrial",
                                                    "Ingeniería Industrial","Ingeniería Mecánica", "Ingeniería Química","Matemática Computacional", "Química");
        if (! degreeValues.contains(student.getDegree()))
            errors.rejectValue("degree","Incorrect value", "Select one of the degrees");

        List<Integer> courseValues = Arrays.asList(1,2,3,4);
        if (!courseValues.contains(student.getCourse()))
            errors.rejectValue("course","Incorrect value", "Select one of the courses");

        if (student.getEmail().trim().equals("") || !student.getEmail().contains("@"))
            errors.rejectValue("email","Incorrect value", "Introduce a valid email");

    }
}
