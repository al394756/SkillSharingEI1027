package com.example.SkillSharingEI1027.controller;

import com.example.SkillSharingEI1027.dao.OfferDao;
import com.example.SkillSharingEI1027.dao.SkillDao;
import com.example.SkillSharingEI1027.modelo.Offer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/Offer")
public class OfferController {
    private OfferDao offerDao;

    @Autowired
    public void setOfferDao(OfferDao offerDao) {
        this.offerDao = offerDao;
    }

    @RequestMapping("/list")
    public String listOffers(Model model){
        model.addAttribute("offers",offerDao.getOffers());
        return "offer/list";
    }

    @RequestMapping("/skills")
    public String listSkills(Model model) {
        SkillDao skillDao=new SkillDao();
        model.addAttribute("skills",skillDao.getSkills());
        return "offer/add";
    }

    @RequestMapping(value = "add")
    public String addOffer(Model model){
        model.addAttribute("offer",new Offer());
        return "offer/add";
    }

    @RequestMapping(value="/add", method = RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("offer") Offer offer, BindingResult bindingResult){
        if (bindingResult.hasErrors())
            return "offer/add";
        offerDao.addOffer(offer);
        return "redirect:list";
    }

    @RequestMapping(value="/update/{idOffer}", method = RequestMethod.GET)
    public String editDescripcionSkill(Model model, @PathVariable String idOffer){
        model.addAttribute("skill", offerDao.getOffer(idOffer));
        return "offer/update";
    }

    @RequestMapping(value="/update", method=RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("offer") Offer offer, BindingResult bindingResult){
        if (bindingResult.hasErrors())
            return "offer/update";
        offerDao.updateOffer(offer);
        return "redirect:list";
    }

    @RequestMapping(value = "/delete/{idOffer}")
    public String processDelete(@PathVariable String idOffer) {
        offerDao.deleteOffer(idOffer);
        return "redirect:../../list";
    }
}
