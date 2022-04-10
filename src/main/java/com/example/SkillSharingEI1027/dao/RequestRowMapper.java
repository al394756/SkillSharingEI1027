package com.example.SkillSharingEI1027.dao;

import com.example.SkillSharingEI1027.modelo.Request;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public final class RequestRowMapper implements RowMapper<Request>{
    @Override
    public Request mapRow(ResultSet rs, int rowNum) throws SQLException {
        Request request=new Request();
        request.setIdRequest(rs.getString("idRequest"));
        request.setStartDate(rs.getObject("startDate", LocalDate.class));
        request.setEndDate(rs.getObject("endDate",LocalDate.class));
        request.setDescription(rs.getString("description"));
        request.setIdStudent(rs.getString("idStudent"));
        request.setIdSkill(rs.getString("idSkill"));
        return request;
    }
}
