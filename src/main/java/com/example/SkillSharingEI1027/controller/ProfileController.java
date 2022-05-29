package com.example.SkillSharingEI1027.controller;

import com.example.SkillSharingEI1027.dao.CollaborationDao;
import com.example.SkillSharingEI1027.modelo.Collaboration;
import com.example.SkillSharingEI1027.modelo.Student;
import com.example.SkillSharingEI1027.services.CollaborationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ProfileController {
    @Autowired
    private CollaborationDao collaborationDao;
    @Autowired
    private CollaborationService collaborationService;



    @RequestMapping(value = "/profile")
    public String profilePage(Model model, HttpSession session){
        Student user = (Student) session.getAttribute("user");

        if (user == null || !user.isActiveAccount()){
            return "welcome";
        }

        List<Collaboration> list= collaborationDao.getCollaborationsDe(user);
        model.addAttribute("sorter", "");



        model.addAttribute("misCollaborations", list);

        session.setAttribute("backUrl", "/profile");

        return "profile";

    }

    @RequestMapping(value = "/profile/filtered")
    public String profilePageFiltered(Model model, HttpSession session, @RequestParam("sorter") String sorter){
        Student user = (Student) session.getAttribute("user");

        if (user == null || !user.isActiveAccount()){
            return "welcome";
        }

        List<Collaboration> list = collaborationService.sorteredList(sorter, user);


        model.addAttribute("sorter",sorter);
        model.addAttribute("misCollaborations", list);

        return "profile";

    }


}
