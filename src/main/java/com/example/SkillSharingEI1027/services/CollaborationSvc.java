package com.example.SkillSharingEI1027.services;

import com.example.SkillSharingEI1027.dao.CollaborationDao;
import com.example.SkillSharingEI1027.dao.MessageDao;
import com.example.SkillSharingEI1027.dao.OffeRequestDao;
import com.example.SkillSharingEI1027.dao.StudentDao;
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
    public void mensajeConfirmacion(Chat chat, String msgContent, Student student){

        Message msg = new Message();
        msg.setDate(LocalDate.now());
        msg.setStudent(student.getIdStudent());
        msg.setIdChat(chat.getIdChat());
        msg.setNumber(messageDao.messageNumber(msg.getIdChat()));
        msg.setContent(msgContent);
        messageDao.addMessage(msg);
    }
}
