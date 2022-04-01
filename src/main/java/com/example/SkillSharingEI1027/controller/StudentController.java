package com.example.SkillSharingEI1027.controller;

import com.example.SkillSharingEI1027.dao.StudentDao;
import com.example.SkillSharingEI1027.modelo.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

class StudentValidator extends StudentController implements Validator {
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

        if (student.getDegree().equals("Select a degree") || ! degreeValues.contains(student.getDegree()))
            errors.rejectValue("degree","Incorrect value", "Select one of the degrees");

        if (!courseValues.contains(student.getCourse()))
            errors.rejectValue("course","Incorrect value", "Select one of the courses");

        if (student.getEmail().trim().equals("") || !student.getEmail().contains("@"))
            errors.rejectValue("email","Incorrect value", "Introduce a valid email");

    }
}
@Controller
public class StudentController {

    protected List<String> degreeValues = Arrays.asList("Select a degree","Ingeniería Informática","Diseño y Desarrollo de Videojuegos","Ingeniería Eléctrica","Arquitectura Técnica","Ingeniería en Diseño Industrial",
            "Ingeniería Industrial","Ingeniería Mecánica", "Ingeniería Química","Matemática Computacional", "Química");
    protected List<Integer> courseValues = Arrays.asList(1,2,3,4);


    @Autowired
    private StudentDao studentDao;

    @RequestMapping("/login")
    public String login(Model model){
        model.addAttribute("user", new StudentDao());
        return "login";
    }

    @RequestMapping(value="/login", method = RequestMethod.POST)
    public String checkLogin(@ModelAttribute("user") Student student, BindingResult bindingResult, HttpSession session){
        StudentValidator validator = new StudentValidator();
        validator.validate(student, bindingResult);
        if (bindingResult.hasErrors())
            return "login";

        student = studentDao.loadUserById(student.getIdStudent(),student.getPassword());

        if (student == null){
            bindingResult.rejectValue("password","badpw","Incorrect Password");
            return "login";
        }
        session.setAttribute("user",student);

        return "redirect:/";
    }

    @RequestMapping(value = "/register")
    public String register(Model model){
        model.addAttribute("student", new Student());
        model.addAttribute("courseList", courseValues);
        model.addAttribute("degreeList", degreeValues);
        return "register";
    }
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String processRegister(@ModelAttribute("student") Student student, BindingResult bindingResult){
        StudentValidator studentValidator = new StudentValidator();
        studentValidator.validate(student, bindingResult);
        if (bindingResult.hasErrors()) {
            System.out.println("b");
            return "redirect:/register";
        }
        studentDao.registerStudent(student);
        System.out.println("a");
        return "redirect:/";
    }

    @RequestMapping("/student/list")
    public String listStudents(Model model){
        model.addAttribute("students", studentDao.getStudentsActivos());
        return "student/list";
    }
}
