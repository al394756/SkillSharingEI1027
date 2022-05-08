package com.example.SkillSharingEI1027.services;

import com.example.SkillSharingEI1027.modelo.Collaboration;
import com.example.SkillSharingEI1027.modelo.OffeRequest;
import com.example.SkillSharingEI1027.modelo.Student;
import org.springframework.context.annotation.Bean;

import java.util.List;

public interface CollaborationService {
    public List<Collaboration> giveCollaborationPendiente(String idStudent);
    public List<Collaboration> giveCollaborations(String idStudent);
    public List<OffeRequest> giveOffeRequestPendientes(String type, Student student);
}
