package com.example.SkillSharingEI1027.services;

import com.example.SkillSharingEI1027.modelo.Collaboration;
import com.example.SkillSharingEI1027.modelo.OffeRequest;
import com.example.SkillSharingEI1027.modelo.Student;

import java.util.List;

public interface CollaborationService {
    List<Collaboration> giveCollaborationPendiente(String idStudent);
    List<Collaboration> giveCollaborations(String idStudent);
    List<OffeRequest> giveOffeRequestPendientes(String type, Student student);
}
