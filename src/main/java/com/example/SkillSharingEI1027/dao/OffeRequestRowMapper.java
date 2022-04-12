package com.example.SkillSharingEI1027.dao;

import com.example.SkillSharingEI1027.modelo.OffeRequest;
import com.example.SkillSharingEI1027.modelo.Offer;
import com.example.SkillSharingEI1027.modelo.Request;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public final class OffeRequestRowMapper implements RowMapper<OffeRequest>{
    private String table;
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
        offeRequest.getStudent().setIdStudent(rs.getString("idStudent"));
        offeRequest.getSkill().setIdSkill(rs.getString("idSkill"));
        return offeRequest;
    }
}
