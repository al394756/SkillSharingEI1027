package com.example.SkillSharingEI1027.controller;


import com.example.SkillSharingEI1027.dao.*;
import com.example.SkillSharingEI1027.modelo.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping("/collaboration")
public class CollaborationController {
    private CollaborationDao collaborationDao;
    private OffeRequestDao offeRequestDao;
    private ChatDao chatDao;
    private MessageDao messageDao;
    private SkillDao skillDao;
    private StudentDao studentDao;

    @Autowired
    public void setOffeRequestDao(OffeRequestDao offeRequestDao, ChatDao chatDao, MessageDao messageDao, SkillDao skillDao, StudentDao studentDao) {
        this.offeRequestDao = offeRequestDao;
        this.chatDao = chatDao;
        this.messageDao=messageDao;
        this.skillDao=skillDao;
        this.studentDao=studentDao;
    }

    @Autowired
    public void setCollaborationDao(CollaborationDao collaborationDao){ this.collaborationDao = collaborationDao;  }

    @RequestMapping("/list")
    public String listCollaborations(Model model, HttpSession session){
        Student user = (Student) session.getAttribute("user");
        if (user == null || !user.isActiveAccount()){
            return "welcome";
        }
        List<Collaboration> collaborations = collaborationDao.getCollaborations();
        model.addAttribute("collaborations", collaborations);

        return "collaboration/list";
    }

    @RequestMapping(value = "/add/{offeRequestId}")
    public String addCollaboration(@PathVariable String offeRequestId, HttpSession session){
        Student student = (Student) session.getAttribute("user");
        if (student == null || !student.isActiveAccount()){
            return "welcome";
        }
        OffeRequest offeRequestAceptada = offeRequestDao.getOffeRequest(offeRequestId);
        OffeRequest offeRequestNueva = crearContrarioA(offeRequestAceptada,student);
        Skill skill= skillDao.getSkill(offeRequestAceptada.getSkill().getIdSkill());


        Collaboration collaboration;
        String msgContent;
        if (offeRequestNueva.getType().equals("Request")) {
            collaboration = new Collaboration(offeRequestNueva, offeRequestAceptada);
            collaboration.setRequestinicia(true);
            msgContent="I have accepted your offer of "+ skill.getName()+" ("+offeRequestAceptada.getId()+")\nAccept the collaboration in the 'Collaboration proposals' section.";
        }
        else{
            collaboration=new Collaboration(offeRequestAceptada,offeRequestNueva);
            collaboration.setRequestinicia(false);

            msgContent="I have accepted your request of "+ skill.getName()+" ("+offeRequestAceptada.getId()+")\nAccept the collaboration in the 'Collaboration proposals' section.";

        }

        Chat chat = conseguirChat(offeRequestAceptada.getStudent(),student);

        mensajeConfirmacion(chat, msgContent, student );


        collaborationDao.addCollaboration(collaboration);

        return "welcome";
    }

    @RequestMapping(value = "/confirm/{idCollaboration}")
    public String confirmCollaboration(@PathVariable String idCollaboration, HttpSession session){
        Collaboration collaboration = collaborationDao.getCollaboration(idCollaboration);

        Student student = (Student) session.getAttribute("user");
        Student student1 = conseguirOtroStudent(student, collaboration);
        Chat chat = conseguirChat(student, student1);
        String msgContent =" I have accepted your collaboration proposal on "+collaboration.getIdRequest().getSkill().getName()+". I hope to see you between "+collaboration.getIdRequest().getStartDate()+" and "+collaboration.getIdOffer().getEndDate();
        mensajeConfirmacion(chat,msgContent,student);
        collaborationDao.confirmCollaboration(collaboration);
        return "redirect:/profile/";
    }

    @RequestMapping(value = "/cancel/{idCollaboration}")
    public String cancelCollaboration(@PathVariable String idCollaboration, HttpSession session){
        Collaboration collaboration = collaborationDao.getCollaboration(idCollaboration);
        Student student = (Student) session.getAttribute("user");
        Student student1 = conseguirOtroStudent(student, collaboration);
        Chat chat = conseguirChat(student, student1);
        String msgContent;
        if (collaboration.getCollaborationState() == 0){
            msgContent = "Sorry I can't accept your collaboration proposal on "+collaboration.getIdRequest().getSkill().getName();
        } else{
            msgContent ="Sorry I have cancelled our planned collaboration on "+collaboration.getIdRequest().getSkill().getName()+" between  "+collaboration.getIdRequest().getStartDate()+" and "+collaboration.getIdOffer().getEndDate();
        }
        mensajeConfirmacion(chat,msgContent,student);
        collaborationDao.cancelCollaboration(collaboration);

        return "redirect:/profile/";
    }

    @RequestMapping(value = "/valorar/{idCollaboration}")
    public String valorarCollaboration(@PathVariable String idCollaboration, HttpSession session, Model model){
        Collaboration collaboration = collaborationDao.getCollaboration(idCollaboration);
        model.addAttribute("collaboration", collaboration);
        return "redirect:/collaboration/valorar";
    }

    private Student conseguirOtroStudent(Student student, Collaboration collaboration){
        if (collaboration.getIdOffer().getStudent().getIdStudent().equals(student.getIdStudent()))
            return collaboration.getIdRequest().getStudent();
        else
            return collaboration.getIdOffer().getStudent();
    }
    private Chat conseguirChat(Student student1, Student student2){
        Chat chat = chatDao.getChatEntreStudents(student1, student2);
        if (chat == null){
            String id=chatDao.createChat(student1, student2);
            chat = chatDao.getChatConId(id);
        }
        if (chat.getUser1().equals(student1.getIdStudent())){
            chat.setNewMsgParaStudent1(true);
        } else if(chat.getUser2().equals(student1.getIdStudent())){
            chat.setNewMsgParaStudent2(true);
        }
        chatDao.updateChat(chat);
        return chat;
    }

    private OffeRequest crearContrarioA(OffeRequest offeRequestAceptada, Student student){
        OffeRequest offeRequestNueva;
        if (offeRequestAceptada.getId().startsWith("rq")){
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

        offeRequestNueva.setStudent(student);
        offeRequestDao.add(offeRequestNueva);
        return offeRequestNueva;

    }
    private void mensajeConfirmacion(Chat chat, String msgContent, Student student){

        Message msg = new Message();
        msg.setDate(LocalDate.now());
        msg.setStudent(student.getIdStudent());
        msg.setIdChat(chat.getIdChat());
        msg.setNumber(messageDao.messageNumber(msg.getIdChat()));
        msg.setContent(msgContent);
        messageDao.addMessage(msg);
    }


}
