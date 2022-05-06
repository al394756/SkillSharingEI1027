package com.example.SkillSharingEI1027.controller;

import com.example.SkillSharingEI1027.dao.OffeRequestDao;
import com.example.SkillSharingEI1027.dao.SkillDao;
import com.example.SkillSharingEI1027.dao.StudentDao;
import com.example.SkillSharingEI1027.modelo.*;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

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
        List<OffeRequest> lista = offeRequestDao.getActiveOffeRequests(type);
        for (OffeRequest r : lista){
            r.setSkill(skillDao.getSkill(r.getSkill().getIdSkill()));
        }
        model.addAttribute("list", lista);
        return "offeRequest/list";
    }

    public String add(Model model,String type) {
        model.addAttribute("type",type);
        List<String> skills = new ArrayList<>();
        skills.add("Select one option");
        for (Skill skill : skillDao.getSkills())
            skills.add(skill.getName());
        model.addAttribute("skills", skills);
        return "offeRequest/add";
    }

    public String processAddSubmit(OffeRequest offeRequest, BindingResult bindingResult, HttpSession session) {
        OffeRequestValidator offeRequestValidator=new OffeRequestValidator();
        offeRequestValidator.validate(offeRequest,bindingResult);
        if (bindingResult.hasErrors())
            return "offeRequest/add";
        Student student = (Student) session.getAttribute("user");
        offeRequest.setStudent(student);
        offeRequest.setSkill(skillDao.getIdBySkill(offeRequest.getSkill().getIdSkill()));
        offeRequestDao.add(offeRequest);
        return "redirect:list";
    }

    public String edit(Model model,String id,String type, HttpSession session){
        model.addAttribute("type",type);
        OffeRequest offeRequest= offeRequestDao.getOffeRequest(id);
        offeRequest.setSkill(skillDao.getSkill(offeRequest.getSkill().getIdSkill()));
        offeRequest.setStudent(studentDao.getStudentUsingId(offeRequest.getStudent().getIdStudent()));
        OffeRequest offeRequestCopia;
        if (type.equals("Request"))
            offeRequestCopia= new Request(offeRequest);
        else
            offeRequestCopia=new Offer(offeRequest);
        System.out.println(offeRequestCopia.toString());

        session.setAttribute("skill",offeRequestCopia.getSkill());
        model.addAttribute("url",offeRequestCopia.getUrl());

        session.setAttribute("offeRequest",offeRequestCopia);
        model.addAttribute("offeRequest",offeRequest);
        return "offeRequest/update";
    }

    public String processUpdateSubmit(OffeRequest offeRequest, BindingResult bindingResult, HttpSession session){
        OffeRequest offeRequestAntigua = (OffeRequest) session.getAttribute("offeRequest");
        System.out.println(offeRequest+"TRAS FORM");
        String descripcion = offeRequest.getDescription();
        OffeRequestValidator offeRequestValidator=new OffeRequestValidator();
        offeRequest.setId(offeRequestAntigua.getId());
        Skill skill = (Skill) session.getAttribute("skill");
        skill.setDescription(descripcion);
        offeRequest.setSkill(skill);
        offeRequest.setStudent(offeRequest.getStudent());
        session.removeAttribute("offeRequest");
        session.removeAttribute("skill");

        System.out.println(offeRequest.toString()+"GGGGGGGG");
        offeRequestValidator.validateUpdate(offeRequest,bindingResult);
        if (bindingResult.hasErrors())
            return "offeRequest/update";
        offeRequestDao.update(offeRequest);
        return "redirect:list";
    }

    public String processDelete(String idOffer) {
        offeRequestDao.delete(idOffer);
        return "redirect:../list";
    }
}

class OffeRequestValidator extends OffeRequestMethods implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return OffeRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        OffeRequest offeRequest=(OffeRequest) obj;
        if (offeRequest.getSkill().getIdSkill().trim().equals("Select one option"))
            errors.rejectValue("skill.idSkill", "compulsory", "Introduce a valid Skill");
        if (offeRequest.getStartDate()==null)
            errors.rejectValue("startDate","compulsory","Introduce a date");
        if (offeRequest.getEndDate()==null)
            errors.rejectValue("endDate","compulsory","Introduce a date");
        else if (offeRequest.getStartDate()!=null&&offeRequest.getStartDate().compareTo(offeRequest.getEndDate())>0)
            errors.rejectValue("endDate","compulsory","End Date must be bigger than Start Date");
        if (offeRequest.getSkill().getIdSkill().trim().equals(""))
            errors.rejectValue("description", "compulsory", "Introduce a description");
    }

    public void validateUpdate(Object obj, Errors errors) {
        OffeRequest offeRequest=(OffeRequest) obj;
        if (offeRequest.getStartDate()==null)
            errors.rejectValue("startDate","compulsory","Introduce a date");
        if (offeRequest.getEndDate()==null)
            errors.rejectValue("endDate","compulsory","Introduce a date");
        else if (offeRequest.getStartDate()!=null&&offeRequest.getStartDate().compareTo(offeRequest.getEndDate())<0)
            errors.rejectValue("endDate","compulsory","End Date must be bigger than Start Date");
        if (offeRequest.getDescription().trim().equals(""))
            errors.rejectValue("description", "compulsory", "Introduce a description");
    }
}
