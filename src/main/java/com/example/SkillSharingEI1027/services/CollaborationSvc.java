package com.example.SkillSharingEI1027.services;

import com.example.SkillSharingEI1027.dao.CollaborationDao;
import com.example.SkillSharingEI1027.dao.OffeRequestDao;
import com.example.SkillSharingEI1027.dao.StudentDao;
import com.example.SkillSharingEI1027.modelo.Collaboration;
import com.example.SkillSharingEI1027.modelo.OffeRequest;
import com.example.SkillSharingEI1027.modelo.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


    @Override
    public List<OffeRequest> giveOffeRequestNoAceptadas(Student student, String type) {
        List<OffeRequest> listOffeRActivas = offeRequestDao.getActiveOffeRequests(type);
        List<OffeRequest> listFinal = new LinkedList<>();
        List<String> listId = new LinkedList<>();
        for (OffeRequest of : listOffeRActivas) {
            listId.add(of.getId());
        }
        List<Collaboration> listCollaborations = collaborationDao.getCollaborationsDe(student);
        if (type.equals("Request")) {
            for (Collaboration c : listCollaborations) {
                if (!listId.contains(c.getIdRequest().getId())) {
                    listFinal.add(c.getIdRequest());
                }
            }

        } else {
            for (Collaboration c : listCollaborations) {
                if (!listId.contains(c.getIdOffer().getId())) {
                    listFinal.add(c.getIdOffer());
                }
            }
        }
        return listFinal;
    }
}
