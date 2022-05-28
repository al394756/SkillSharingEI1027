package com.example.SkillSharingEI1027.services;

import com.example.SkillSharingEI1027.modelo.Chat;
import com.example.SkillSharingEI1027.modelo.Collaboration;
import com.example.SkillSharingEI1027.modelo.OffeRequest;
import com.example.SkillSharingEI1027.modelo.Student;

import java.util.List;

public interface CollaborationService {
    List<OffeRequest> giveOffeRequestNoAceptadas(Student student, String type);
    List<OffeRequest> giveOffeRequestNoAceptadas(Student student, String type, String idSkill);
    void mensajeConfirmacion(Chat chat, String msgContent, Student student);
    Chat conseguirChat(Student student1, Student student2);
}
