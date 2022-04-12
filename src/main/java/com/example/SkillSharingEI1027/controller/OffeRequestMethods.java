package com.example.SkillSharingEI1027.controller;

import com.example.SkillSharingEI1027.dao.OffeRequestDao;
import com.example.SkillSharingEI1027.dao.SkillDao;
import com.example.SkillSharingEI1027.dao.StudentDao;
import com.example.SkillSharingEI1027.modelo.OffeRequest;
import com.example.SkillSharingEI1027.modelo.Request;
import com.example.SkillSharingEI1027.modelo.Skill;
import com.example.SkillSharingEI1027.modelo.Student;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

public class OffeRequestMethods <T> {
    private OffeRequestDao offeRequestDao;
    private SkillDao skillDao;
    private StudentDao studentDao;

    public void setOffeRequestDao(OffeRequestDao offeRequestDao, SkillDao skillDao, StudentDao studentDao) {
        this.offeRequestDao = offeRequestDao;
        this.skillDao = skillDao;
        this.studentDao=studentDao;
    }

    public String list(Model model, String type) {
        model.addAttribute("type",type);
        model.addAttribute("list", offeRequestDao.getActiveOffeRequests(type));
        return "offeRequest/list";
    }

    public String add(Model model,String type) {
        model.addAttribute("type",type);
        List<String> skills = new ArrayList<>();
        for (Skill skill : skillDao.getSkills())
            skills.add(skill.getName());
        model.addAttribute("skills", skills);
        return "offeRequest/add";
    }

    public String processAddSubmit(OffeRequest offeRequest, BindingResult bindingResult, HttpSession session) {
        if (bindingResult.hasErrors())
            return "redirect/add";
        Student student = (Student) session.getAttribute("user");
        offeRequest.setStudent(student);
        offeRequest.setSkill(skillDao.getIdBySkill(offeRequest.getSkill().getIdSkill()));
        offeRequestDao.add(offeRequest);
        return "redirect:list";
    }

    public String edit(Model model,String id,String type){
        model.addAttribute("type",type);
        OffeRequest offeRequest= offeRequestDao.getOffeRequest(id);
        offeRequest.setSkill(skillDao.getSkill(offeRequest.getSkill().getIdSkill()));
        offeRequest.setStudent(studentDao.getStudentUsingId(offeRequest.getStudent().getIdStudent()));
        model.addAttribute("offeRequest",offeRequest);
        System.out.println(offeRequest);
        return "offeRequest/update";
    }

    public String processUpdateSubmit(OffeRequest offeRequest, BindingResult bindingResult){
        if (bindingResult.hasErrors())
            return "offeRequest/update";
        System.out.println(offeRequest);
        offeRequestDao.update(offeRequest);
        return "redirect:list";
    }

    public String processDelete(String idOffer) {
        offeRequestDao.delete(idOffer);
        return "redirect:../list";
    }
}
