package com.example.SkillSharingEI1027.controller;

import com.example.SkillSharingEI1027.dao.StudentDao;
import com.example.SkillSharingEI1027.modelo.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

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
    public String processRegister(@ModelAttribute("student") Student student, BindingResult bindingResult){
        StudentValidator studentValidator = new StudentValidator();
        studentValidator.validate(student, bindingResult);
        if (bindingResult.hasErrors())
            return "register";
        studentDao.registerStudent(student);
        return "redirect:/";
    }
}
