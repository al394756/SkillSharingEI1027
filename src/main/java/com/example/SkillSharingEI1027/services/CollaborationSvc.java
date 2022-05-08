package com.example.SkillSharingEI1027.services;

import com.example.SkillSharingEI1027.dao.CollaborationDao;
import com.example.SkillSharingEI1027.dao.OffeRequestDao;
import com.example.SkillSharingEI1027.dao.StudentDao;
import com.example.SkillSharingEI1027.modelo.Collaboration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class CollaborationSvc {

    @Autowired
    StudentDao studentDao;

    @Autowired
    CollaborationDao collaborationDao;

    @Autowired
    OffeRequestDao offeRequestDao;

    public List<Collaboration> giveCollaborationPendiente(String idStudent){
        List<Collaboration> lista = collaborationDao.getCollaborations();
        List<Collaboration> lista_pendientes = new LinkedList<>();
        for (Collaboration c:lista){
            if (c.getCollaborationState() == 0){
                if (c.getIdOffer().getStudent().getIdStudent().equals(idStudent) || c.getIdRequest().getStudent().getIdStudent().equals(idStudent)){
                    lista_pendientes.add(c);
                }
            }
        }
        return lista_pendientes;
    }
}
