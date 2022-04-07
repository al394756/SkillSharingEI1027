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
import java.util.Locale;

class StudentValidator extends StudentController implements Validator {
    private List<Integer> courseValues = Arrays.asList(1,2,3,4);
    private List<String> degreeValues = Arrays.asList("Ingeniería Informática","Diseño y Desarrollo de Videojuegos","Ingeniería Eléctrica","Arquitectura Técnica","Ingeniería en Diseño Industrial",
            "Ingeniería Industrial","Ingeniería Mecánica", "Ingeniería Química","Matemática Computacional", "Química");

    @Override
    public boolean supports(Class<?> clazz) {
        return Student.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        Student student = (Student) obj;
        if (student.getName().trim().equals(""))
            errors.rejectValue("name", "compulsory", "Introduce a valid name");

        if (student.getSurname().trim().equals(""))
            errors.rejectValue("surname", "compulsory", "Introduce a valid surname");

        String telefono =student.getPhoneNumber()+"";
        if (telefono.trim().equals("") || telefono.length()!=8)
            errors.rejectValue("phoneNumber", "compulsory", "Introduce a valid phone number");

        if (student.getDni().trim().equals("") || ! dniValidator(student.getDni().toUpperCase(Locale.ROOT)) )
            errors.rejectValue("dni","compulsory", "Introduce a valid DNI");

        if (student.getPassword().trim().equals(""))
            errors.rejectValue("password","compulsory","Introduce a valid password");

        if (! degreeValues.contains(student.getDegree()))
            errors.rejectValue("degree","Incorrect value", "Select one of the degrees");

        if (!courseValues.contains(student.getCourse()))
            errors.rejectValue("course","Incorrect value", "Select one of the courses");

        if (student.getEmail().trim().equals("") || !student.getEmail().contains("@"))
            errors.rejectValue("email","Incorrect value", "Introduce a valid email");


    }

    public boolean dniValidator(String dni){
        if (dni.length()!=9)
            return false;

        //Comprobamos formato NIE válido
        if (dni.charAt(0) == 'X' || dni.charAt(0) == 'T' ){
            try{
                Integer.parseInt(dni.substring(1, 8));
            } catch (Exception ex){
                return false;
            }
            char letra = dni.charAt(8);
            if (! (letra >= 'A' && letra <= 'Z')){
                return false;
            }
        }
        //Comprobamos formato DNI válido
        else{
            try {
                Integer.parseInt(dni.substring(0, 8));
            } catch (Exception ex){
                return false;
            }
            char letra = dni.charAt(8);
            if (! (letra >= 'A' && letra <= 'Z')){
                return false;
            }

        }
        return true;
    }
}
@Controller
public class StudentController {





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
        return "register";
    }
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String processRegister(@ModelAttribute("student") Student student, BindingResult bindingResult,HttpSession session){

        StudentValidator studentValidator = new StudentValidator();
        studentValidator.validate(student, bindingResult);
        if (bindingResult.hasErrors()) {
            return "/register";
        }
        studentDao.registerStudent(student);
        session.setAttribute("user",student);

        return "index";
    }

    @RequestMapping("/student/list")
    public String listStudents(Model model){
        model.addAttribute("students", studentDao.getStudentsActivos());
        return "student/list";
    }
}
