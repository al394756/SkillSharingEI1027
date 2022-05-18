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
import java.time.LocalDate;
import java.util.ArrayList;
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
        collaborations = conseguirDatosCollaborations(collaborations);
        model.addAttribute("collaborations", collaborations);

        return "collaboration/list"; //falta html
    }

    @RequestMapping(value = "/add/{offeRequestId}")
    public String addCollaboration(@PathVariable String offeRequestId, HttpSession session){

        OffeRequest offeRequestAceptada = offeRequestDao.getOffeRequest(offeRequestId);

        Student student = (Student) session.getAttribute("user");
        if (student == null || !student.isActiveAccount()){
            return "welcome";
        }
        OffeRequest offeRequestNueva = crearContrarioA(offeRequestAceptada,student);
        Skill skill= skillDao.getSkill(offeRequestAceptada.getSkill().getIdSkill());


        Collaboration collaboration;
        String msgContent;
        if (offeRequestNueva.getType().equals("Request")) {
            collaboration = new Collaboration(offeRequestNueva, offeRequestAceptada);
            msgContent="I have accepted your offer of "+ skill.getName()+" ("+offeRequestAceptada.getId()+")\nAccept the collaboration in the 'Collaboration proposals' section.";
        }
        else{
            collaboration=new Collaboration(offeRequestAceptada,offeRequestNueva);
            msgContent="I have accepted your request of "+ skill.getName()+" ("+offeRequestAceptada.getId()+")\nAccept the collaboration in the 'Collaboration proposals' section.";

        }

        Chat chat = conseguirChat(offeRequestAceptada.getStudent(),student);

        mensajeConfirmacion(chat, msgContent, student );


        collaborationDao.addCollaboration(collaboration);

        return "welcome";
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
    private List<Collaboration> conseguirDatosCollaborations(List<Collaboration> collaborations){
        List<Collaboration> collabFinal = new LinkedList<>();
        for (Collaboration c: collaborations){
            OffeRequest offeRequest = offeRequestDao.getOffeRequest(c.getIdOffer().getId());
            offeRequest.setSkill(skillDao.getSkill(offeRequest.getSkill().getIdSkill()));
            offeRequest.setStudent(studentDao.getStudentUsingId(offeRequest.getStudent().getIdStudent()));
            c.setIdOffer(offeRequest);

            offeRequest =offeRequestDao.getOffeRequest(c.getIdRequest().getId());
            offeRequest.setSkill(skillDao.getSkill(offeRequest.getSkill().getIdSkill()));
            offeRequest.setStudent(studentDao.getStudentUsingId(offeRequest.getStudent().getIdStudent()));
            c.setIdRequest(offeRequest);

            collabFinal.add(c);
        }
        return collabFinal;
    }
}
