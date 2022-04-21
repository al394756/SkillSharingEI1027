package com.example.SkillSharingEI1027.controller;


import com.example.SkillSharingEI1027.dao.CollaborationDao;
import com.example.SkillSharingEI1027.dao.OffeRequestDao;
import com.example.SkillSharingEI1027.dao.SkillDao;
import com.example.SkillSharingEI1027.dao.StudentDao;
import com.example.SkillSharingEI1027.modelo.Collaboration;
import com.example.SkillSharingEI1027.modelo.OffeRequest;

import com.example.SkillSharingEI1027.modelo.Offer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/collaboration")
public class CollaborationController {
    private CollaborationDao collaborationDao;
    private OffeRequestDao offeRequestDao;
    private SkillDao skillDao;
    private StudentDao studentDao;

    @Autowired
    public void setOffeRequestDao(OffeRequestDao offeRequestDao, SkillDao skillDao, StudentDao studentDao) {
        this.offeRequestDao = offeRequestDao;
        this.skillDao = skillDao;
        this.studentDao=studentDao;
    }

    @Autowired
    public void setCollaborationDao(CollaborationDao collaborationDao){
        this.collaborationDao = collaborationDao;
    }

    @RequestMapping("/list")
    public String listCollaborations(Model model){
        model.addAttribute("collaborations",collaborationDao.getCollaborations());
        return "collaboration/list"; //falta html
    }

    @RequestMapping(value = "add")
    public String addCollaboration(Model model){
        setOffeRequestDao(offeRequestDao,skillDao,studentDao);
        model.addAttribute("collaboration",new Collaboration());
        List<String> requestList = new ArrayList<>();
        for (OffeRequest request : offeRequestDao.getActiveOffeRequests("Request"))
            requestList.add(request.getId());
        model.addAttribute("requests", requestList);

        List<String> offersList = new ArrayList<>();
        for (OffeRequest offer : offeRequestDao.getActiveOffeRequests("Offer"))
            offersList.add(offer.getId());
        model.addAttribute("offers", offersList);
        return "collaboration/add"; //falta html
    }

    @RequestMapping(value="/add", method = RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("request") Collaboration collaboration, BindingResult bindingResult){
        if (bindingResult.hasErrors())
            return "collaboration/add";
        collaborationDao.addCollaboration(collaboration);
        return "redirect:list";
    }

    @RequestMapping(value="/update/{idCollaboration}", method = RequestMethod.GET)
    public String editCollaboration(Model model, @PathVariable String idCollaboration){
        model.addAttribute("collaboration", collaborationDao.getCollaboration(idCollaboration));
        return "collaboration/update"; //falta html
    }

    @RequestMapping(value="/update", method=RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("collaboration") Collaboration collaboration, BindingResult bindingResult){
        if (bindingResult.hasErrors())
            return "collaboration/update"; //falta html
        collaborationDao.updateCollaboration(collaboration);
        return "redirect:list";
    }

    @RequestMapping(value = "/delete/{idCollaboration}")
    public String processDelete(@PathVariable String idCollaboration) {
        collaborationDao.deleteCollaboration(idCollaboration);
        return "redirect:../list"; //falta html
    }










}
