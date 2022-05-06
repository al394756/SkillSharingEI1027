package com.example.SkillSharingEI1027.controller;

import com.example.SkillSharingEI1027.dao.ChatDao;
import com.example.SkillSharingEI1027.dao.MessageDao;
import com.example.SkillSharingEI1027.dao.StudentDao;
import com.example.SkillSharingEI1027.modelo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

class StudentValidator extends StudentController implements Validator {
    private final List<Integer> courseValues = Arrays.asList(1,2,3,4);
    private final List<String> degreeValues = Arrays.asList("Ingeniería Informática","Diseño y Desarrollo de Videojuegos","Ingeniería Eléctrica","Arquitectura Técnica","Ingeniería en Diseño Industrial",
            "Ingeniería Industrial","Ingeniería Mecánica", "Ingeniería Química","Matemática Computacional", "Química");

    private StudentDao studentDao;

    public void setStudentDao(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Student.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        Student student = (Student) obj;
        if (student.getName().trim().equals("") || student.getName() == null)
            errors.rejectValue("name", "compulsory", "Introduce a valid name");
        if (student.getPassword().trim().equals(""))
            errors.rejectValue("password","compulsory","Introduce a valid password");

    }


    public void validateRegister(Object obj, Errors errors) {
        Student student = (Student) obj;

        if (student.getName().trim().equals(""))
            errors.rejectValue("name", "compulsory", "Introduce a valid name");

        if (student.getSurname().trim().equals(""))
            errors.rejectValue("surname", "compulsory", "Introduce a valid surname");

        String telefono =student.getPhoneNumber()+"";
        if (telefono.trim().equals("") || telefono.length()!=9)
            errors.rejectValue("phoneNumber", "compulsory", "Introduce a valid phone number");

        if (student.getDni().trim().equals("") || ! dniValidator(student.getDni().toUpperCase(Locale.ROOT).trim()) )
            errors.rejectValue("dni","compulsory", "Introduce a valid DNI");

        if (student.getPassword().trim().equals(""))
            errors.rejectValue("password","compulsory","Introduce a valid password");

        if (! degreeValues.contains(student.getDegree()))
            errors.rejectValue("degree","Incorrect value", "Select one of the degrees");

        if (!courseValues.contains(student.getCourse()))
            errors.rejectValue("course","Incorrect value", "Select one of the courses");

        if (student.getEmail().trim().equals("") || !student.getEmail().contains("@"))
            errors.rejectValue("email","Incorrect value", "Introduce a valid email");

        if (studentDao.checkExistingStudentDNI(student))
            errors.rejectValue("dni", "Incorrect value", "This DNI is already registered");

        if (studentDao.checkExistingStudentEmail(student))
            errors.rejectValue("email", "Incorrect value", "This email is already registered");

    }

    public void validateBan(Object obj, Errors errors, Student skp){
        Student student = (Student) obj;
        if (student.getIdStudent().equals(skp.getIdStudent())){
            errors.rejectValue("banReason", "Imposible", "You can't ban yourself");
        }
        if (student.getBanReason().trim().equals("") || student.getBanReason()==null){
            errors.rejectValue("banReason","Incorrect value", "Introduce a reason");
        }
        if (student.isSkpMember())
            errors.rejectValue("banReason","Imposible","You can't ban a SKP member");
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
            return letra >= 'A' && letra <= 'Z';
        }
        //Comprobamos formato DNI válido
        else{
            try {
                Integer.parseInt(dni.substring(0, 8));
            } catch (Exception ex){
                return false;
            }
            char letra = dni.charAt(8);
            return letra >= 'A' && letra <= 'Z';

        }
    }
}
@Controller
public class StudentController {


    @Autowired
    private StudentDao studentDao;
    @Autowired
    private ChatDao chatDao;
    @Autowired
    private MessageDao messageDao;

    @RequestMapping("/login")
    public String login(Model model){
        model.addAttribute("user", new Student());
        return "login";
    }

//Al intentar hacer login no sabemos si ha metido email o id. Por esto metemos ese input (sea lo que sea) en el campo name. Después comprobamos si el input es el email o el id y validamos
    @RequestMapping(value="/login", method = RequestMethod.POST)
    public String checkLogin(@ModelAttribute("user") Student student, BindingResult bindingResult, HttpSession session){
        StudentValidator validator = new StudentValidator();
        validator.validate(student, bindingResult);
        if (bindingResult.hasErrors())
            return "login";

        student = studentDao.loadUser(student.getName(),student.getPassword());

        if (student == null){
            bindingResult.rejectValue("password","badpw","There are not any student registered with that combination of id or email and password");
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
        studentValidator.setStudentDao(studentDao);
        studentValidator.validateRegister(student, bindingResult);
        if (bindingResult.hasErrors()) {
            return "/register";
        }
        String id=studentDao.registerStudent(student);
        student = studentDao.getStudentUsingId(id);

        session.setAttribute("user",student);
        return "redirect:/";
    }

    @RequestMapping("/student/list")
    public String listStudents(Model model, HttpSession session){
        model.addAttribute("students", studentDao.getStudentsActivos());
        model.addAttribute("user", session.getAttribute("user"));
        model.addAttribute("sorter", new Sorter());
        model.addAttribute("comparator", new StudentComparatorById());
        return "student/list";
    }

    @RequestMapping("/student/list/sorted")
    public String listStudentsSortered(Model model, HttpSession session, @ModelAttribute("sorter") Sorter sorter){
        StudentComparator comparator;
        if (sorter.getType().equals("Id"))
            comparator= new StudentComparatorById(sorter);
        else
            comparator= new StudentComparatorByBalanceHours(sorter);
        model.addAttribute("sorter",sorter);
        model.addAttribute("comparator",comparator);
        model.addAttribute("students", studentDao.getStudentsActivos());
        model.addAttribute("user", session.getAttribute("user"));
        return "student/list";
    }


    @RequestMapping("/logout")
    public String logOut(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }

    @RequestMapping(value="/student/ban/{id}", method = RequestMethod.GET)
    public String banStudent(Model model, @PathVariable String id){
        model.addAttribute("student", studentDao.getStudentUsingId(id));
        return "student/ban";
    }

    @RequestMapping(value = "/student/ban", method = RequestMethod.POST)
    public String processBanSubmit(@ModelAttribute("student") Student student, BindingResult bindingResult, HttpSession session){

        Student user = (Student) session.getAttribute("user");
        StudentValidator studentValidator= new StudentValidator();
        studentValidator.validateBan(student, bindingResult, user);
        if (bindingResult.hasErrors())
            return "student/ban";

        String msg = "Our SKP: "+user.getName()+"("+user.getIdStudent()+") has banned you from this service because:\n"+student.getBanReason();
        student.setBanReason(msg);
        String idChat =chatDao.createChat(studentDao.getStudentUsingId("id000000"), student);
        messageDao.banMessage(idChat, student);
        studentDao.cancelStudent(student);
        return "redirect:/";
    }

    @RequestMapping(value = "/profile/{id}", method=RequestMethod.GET)
    public String profilePage(@PathVariable String id){
        return "/profile";
    }
}
