package com.example.SkillSharingEI1027.dao;

import com.example.SkillSharingEI1027.modelo.*;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public final class CollaborationRowMapper implements RowMapper<Collaboration> {
    @Override
    public Collaboration mapRow(ResultSet rs, int rowNum) throws SQLException {
        Collaboration collaboration = new Collaboration();
        collaboration.setIdCollaboration(rs.getString("idCollaboration"));
        collaboration.setAssessmentScore(rs.getInt("AssessmentScore"));
        collaboration.setCollaborationState(rs.getInt("CollaborationState"));
        collaboration.setHours(rs.getInt("hours"));
        collaboration.setRequestinicia(rs.getBoolean("requestinicia"));


        Skill skill =new Skill();
        skill.setName(rs.getString(21));
        skill.setIdSkill(rs.getString(20));
        skill.setDescription(rs.getString(22));
        skill.setLevel(rs.getInt(23));

        Student studentReq = new Student();
        studentReq.setIdStudent(rs.getString(24));
        studentReq.setDni(rs.getString(25));
        studentReq.setName(rs.getString(26));
        studentReq.setEmail(rs.getString(27));
        studentReq.setPhoneNumber(rs.getInt(28));
        studentReq.setPassword(rs.getString(29));
        studentReq.setDegree(rs.getString(30));
        studentReq.setCourse(rs.getInt(31));
        studentReq.setBalanceHours(rs.getInt(32));
        studentReq.setSkpMember(rs.getBoolean(33));
        studentReq.setActiveAccount(rs.getBoolean(34));
        studentReq.setBanReason(rs.getString(35));

        Request request=new Request();
        request.setId(rs.getString("idRequest"));
        request.setStudent(studentReq);
        request.setSkill(skill);
        request.setDescription(rs.getString(12));
        request.setStartDate(rs.getObject(10,LocalDate.class));
        request.setEndDate(rs.getObject(11,LocalDate.class));

        Student studentOffer = new Student();
        studentOffer.setIdStudent(rs.getString(36));
        studentOffer.setDni(rs.getString(37));
        studentOffer.setName(rs.getString(38));
        studentOffer.setEmail(rs.getString(39));
        studentOffer.setPhoneNumber(rs.getInt(40));
        studentOffer.setPassword(rs.getString(41));
        studentOffer.setDegree(rs.getString(42));
        studentOffer.setCourse(rs.getInt(43));
        studentOffer.setBalanceHours(rs.getInt(44));
        studentOffer.setSkpMember(rs.getBoolean(45));
        studentOffer.setActiveAccount(rs.getBoolean(46));
        studentOffer.setBanReason(rs.getString(47));



        Offer offer = new Offer();
        offer.setId(rs.getString("idOffer"));
        offer.setSkill(skill);
        offer.setStudent(studentOffer);
        offer.setDescription(request.getDescription());
        offer.setStartDate(request.getStartDate());
        offer.setEndDate(request.getEndDate());


        collaboration.setIdRequest(request);
        collaboration.setIdOffer(offer);

        return collaboration;
    }
}
