package com.example.SkillSharingEI1027.controller;

import com.example.SkillSharingEI1027.dao.CollaborationDao;
import com.example.SkillSharingEI1027.dao.SkillDao;
import com.example.SkillSharingEI1027.modelo.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;

@Controller
@RequestMapping("/")
public class IndexController {

    private CollaborationDao collaborationDao;

    @Autowired
    public void setSkillDao(CollaborationDao collaborationDao){ this.collaborationDao=collaborationDao;}

    @RequestMapping("/")
    public String indexPage(HttpSession session){
        session.setAttribute("today", LocalDate.now());
        collaborationDao.actualizaCollaborations(LocalDate.now());
        Student user = (Student)session.getAttribute("user");
        if ( user == null  || !user.isActiveAccount())
            return "welcome";
        return "index";
    }
}
