package com.example.SkillSharingEI1027.controller;


import com.example.SkillSharingEI1027.dao.*;
import com.example.SkillSharingEI1027.modelo.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/collaboration")
public class CollaborationController {
    private CollaborationDao collaborationDao;
    private OffeRequestDao offeRequestDao;
    private ChatDao chatDao;
    private MessageDao messageDao;

    @Autowired
    public void setOffeRequestDao(OffeRequestDao offeRequestDao, ChatDao chatDao, MessageDao messageDao) {
        this.offeRequestDao = offeRequestDao;
        this.chatDao = chatDao;
        this.messageDao=messageDao;
    }

    @Autowired
    public void setCollaborationDao(CollaborationDao collaborationDao){ this.collaborationDao = collaborationDao;  }

    @RequestMapping("/list")
    public String listCollaborations(Model model){
        model.addAttribute("collaborations",collaborationDao.getCollaborations());
        return "collaboration/list"; //falta html
    }

    @RequestMapping(value = "/add/{offeRequestId}")
    public String addCollaboration(@PathVariable String offeRequestId, HttpSession session){
        OffeRequest offeRequestNueva;
        OffeRequest offeRequestAceptada = offeRequestDao.getOffeRequest(offeRequestId);
        if (offeRequestId.substring(0,2).equals("rq")){
            offeRequestNueva=new Offer(offeRequestAceptada);
            offeRequestNueva.setType("Offer");
            offeRequestNueva.setStart("of");
            offeRequestNueva.setUrl("offer");
        } else {
            offeRequestNueva = new Request(offeRequestAceptada);
            offeRequestNueva.setType("Request");
            offeRequestNueva.setStart("rq");
            offeRequestNueva.setUrl("request");
        }
        offeRequestNueva.setStudent((Student) session.getAttribute("user"));
        offeRequestNueva = offeRequestDao.add(offeRequestNueva);

        Collaboration collaboration;
        if (offeRequestNueva.getType().equals("Request"))
            collaboration= new Collaboration(offeRequestNueva,offeRequestAceptada);
        else
            collaboration=new Collaboration(offeRequestAceptada,offeRequestNueva);



        collaborationDao.addCollaboration(collaboration);

        return "welcome";
    }


}
