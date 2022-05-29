package com.example.SkillSharingEI1027.services;

import com.example.SkillSharingEI1027.dao.*;
import com.example.SkillSharingEI1027.modelo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Service
public class CollaborationSvc implements CollaborationService {

    @Autowired
    StudentDao studentDao;

    @Autowired
    CollaborationDao collaborationDao;

    @Autowired
    OffeRequestDao offeRequestDao;

    @Autowired
    MessageDao messageDao;

    @Autowired
    ChatDao chatDao;


    @Override
    public List<OffeRequest> giveOffeRequestNoAceptadas(Student student, String type) {
        List<OffeRequest> listOffeRActivas = offeRequestDao.getActiveOffeRequests(type);
        List<OffeRequest> listFinal = new LinkedList<>();
        List<String> listId = new LinkedList<>();
        for (OffeRequest of : listOffeRActivas) {
            listId.add(of.getId());
        }
        List<Collaboration> listCollaborations = collaborationDao.getCollaborationsActivasDe(student);
        if (type.equals("Request")) {
            for (Collaboration c : listCollaborations) {
                if (listId.contains(c.getIdRequest().getId())) {
                    listFinal.add(c.getIdRequest());
                }
            }

        } else {
            for (Collaboration c : listCollaborations) {
                if (listId.contains(c.getIdOffer().getId())) {
                    listFinal.add(c.getIdOffer());
                }
            }
        }
        listOffeRActivas.removeAll(listFinal);
        return listOffeRActivas;
    }

    @Override
    public List<OffeRequest> giveOffeRequestNoAceptadas(Student student, String type, String idSkill) {
        List<OffeRequest> listOffeRActivas = offeRequestDao.getOffeRequestFiltered(type,idSkill);
        List<OffeRequest> listFinal = new LinkedList<>();
        List<String> listId = new LinkedList<>();
        for (OffeRequest of : listOffeRActivas) {
            listId.add(of.getId());
        }
        List<Collaboration> listCollaborations = collaborationDao.getCollaborationsActivasDe(student);
        if (type.equals("Request")) {
            for (Collaboration c : listCollaborations) {
                if (listId.contains(c.getIdRequest().getId())) {
                    listFinal.add(c.getIdRequest());
                }
            }

        } else {
            for (Collaboration c : listCollaborations) {
                if (listId.contains(c.getIdOffer().getId())) {
                    listFinal.add(c.getIdOffer());
                }
            }
        }
        listOffeRActivas.removeAll(listFinal);
        return listOffeRActivas;
    }

    @Override
    public void mensajeConfirmacion(Chat chat, String msgContent, Student student){

        Message msg = new Message();
        msg.setDate(LocalDate.now());
        msg.setStudent(student.getIdStudent());
        msg.setIdChat(chat.getIdChat());
        msg.setNumber(messageDao.messageNumber(msg.getIdChat()));
        msg.setContent(msgContent);
        messageDao.addMessage(msg);
    }

    public Chat conseguirChat(Student student1, Student student2){
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
    public List<Collaboration> sorteredList(String sorter, Student user){
        List<Collaboration> list=null;
        switch (sorter) {
            case "All":
                list = collaborationDao.getCollaborationsDe(user);
                break;
            case "Unfinished":
                list = collaborationDao.getCollaborationsActivasDe(user);
                break;
            case "Finished":
                list = collaborationDao.getCollaborationsAcabadasDe(user);
                break;
        }
        return list;
    }
    public List<Collaboration> sorteredList(String sorter){
        List<Collaboration> list=null;
        switch (sorter) {
            case "All":
                list = collaborationDao.getCollaborations();
                break;
            case "Unfinished":
                list = collaborationDao.getCollaborationsActivas();
                break;
            case "Finished":
                list = collaborationDao.getCollaborationsAcabadas();
                break;
        }
        return list;
    }

    public Student actualizarPuntuacionStudents(Collaboration collaboration){
        Student studentReq = collaboration.getIdRequest().getStudent();
        Student studentOff = collaboration.getIdOffer().getStudent();
        studentDao.actualizarPuntuacionStudent(studentReq, -collaboration.getHours());
        studentDao.actualizarPuntuacionStudent(studentOff, collaboration.getHours());
        return studentDao.getStudentUsingId(studentReq.getIdStudent());

    }
}
