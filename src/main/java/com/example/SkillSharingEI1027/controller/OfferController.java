package com.example.SkillSharingEI1027.controller;

import com.example.SkillSharingEI1027.dao.OffeRequestDao;
import com.example.SkillSharingEI1027.dao.SkillDao;
import com.example.SkillSharingEI1027.dao.StudentDao;
import com.example.SkillSharingEI1027.modelo.Offer;
import com.example.SkillSharingEI1027.services.CollaborationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/offer")
public class OfferController {
    private final OffeRequestMethods<Offer> offeRequestMethods=new OffeRequestMethods<>();
    private final String type="Offer";



    @Autowired
    public void setOffeRequestDao(OffeRequestDao offeRequestDao, SkillDao skillDao, StudentDao studentDao) {
        offeRequestMethods.setOffeRequestDao(offeRequestDao,skillDao,studentDao);
    }

    @RequestMapping("/list")
    public String listOffers(Model model, HttpSession session){
        return offeRequestMethods.list(model,type, session);
    }

    @RequestMapping(value = "/add")
    public String addOffer(Model model,HttpSession session){
        model.addAttribute("offeRequest",new Offer());
        return offeRequestMethods.add(model,type,session);
    }

    @RequestMapping(value="/add", method = RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("offer") Offer offer, BindingResult bindingResult, HttpSession session,Model model){
        return offeRequestMethods.processAddSubmit(offer,bindingResult,session,model);
    }

    @RequestMapping(value="/confirmado")
    public String processConfirmAddSubmit(HttpSession session){
        return offeRequestMethods.processConfirmAddSubmit(session);
    }
    @RequestMapping(value="/update/{id}", method = RequestMethod.GET)
    public String edit(Model model, @PathVariable String id, HttpSession session){
        return offeRequestMethods.edit(model, id,type, session);
    }

    @RequestMapping(value="/update", method=RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("offer") Offer offer, BindingResult bindingResult, HttpSession session){
        return offeRequestMethods.processUpdateSubmit(offer,bindingResult, session);
    }

    @RequestMapping(value = "/cancel/{id}")
    public String processDelete(@PathVariable String id) {
        return offeRequestMethods.processDelete(id);
    }
}

