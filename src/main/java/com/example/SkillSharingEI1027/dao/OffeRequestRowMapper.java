package com.example.SkillSharingEI1027.dao;

import com.example.SkillSharingEI1027.modelo.*;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public final class OffeRequestRowMapper implements RowMapper<OffeRequest>{
    private final String table;
    public OffeRequestRowMapper(String table){
        this.table=table;
    }
    @Override
    public OffeRequest mapRow(ResultSet rs, int rowNum) throws SQLException {
        OffeRequest offeRequest;
        if (table.equals("Request")) offeRequest=new Request();
        else offeRequest=new Offer();
        offeRequest.setId(rs.getString("id"));
        offeRequest.setStartDate(rs.getObject("startDate", LocalDate.class));
        offeRequest.setEndDate(rs.getObject("endDate",LocalDate.class));
        offeRequest.setDescription(rs.getString("description"));
        Student student = new Student();
        student.setIdStudent(rs.getString("idstudent"));
        student.setName(rs.getString(9));
        student.setDegree(rs.getString("dni"));
        student.setEmail(rs.getString("email"));
        student.setPhoneNumber(rs.getInt("phonenumber"));
        student.setPassword(rs.getString("password"));
        student.setDegree(rs.getString("degree"));
        student.setCourse(rs.getInt("course"));
        student.setBalanceHours(rs.getInt("balancehours"));
        student.setSkpMember(rs.getBoolean("skpmember"));
        student.setActiveAccount(rs.getBoolean("activeAccount"));
        student.setBanReason(rs.getString("banreason"));
        Skill skill = new Skill();
        skill.setIdSkill(rs.getString(19));
        skill.setName(rs.getString(20));
        skill.setDescription(rs.getString(21));
        skill.setLevel(rs.getInt("skillLevel"));

        offeRequest.setStudent(student);
        offeRequest.setSkill(skill);
        return offeRequest;
    }
}
