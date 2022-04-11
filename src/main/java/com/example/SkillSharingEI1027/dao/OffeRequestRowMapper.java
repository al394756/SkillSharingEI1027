package com.example.SkillSharingEI1027.dao;

import com.example.SkillSharingEI1027.modelo.OffeRequest;
import com.example.SkillSharingEI1027.modelo.Request;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public final class OffeRequestRowMapper implements RowMapper<OffeRequest>{
    @Override
    public OffeRequest mapRow(ResultSet rs, int rowNum) throws SQLException {
        OffeRequest request=new Request();
        request.setId(rs.getString("id"));
        request.setStartDate(rs.getObject("startDate", LocalDate.class));
        request.setEndDate(rs.getObject("endDate",LocalDate.class));
        request.setDescription(rs.getString("description"));
        request.getStudent().setIdStudent(rs.getString("idStudent"));
        request.getSkill().setIdSkill(rs.getString("idSkill"));
        return request;
    }
}
