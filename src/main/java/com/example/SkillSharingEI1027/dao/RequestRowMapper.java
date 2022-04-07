package com.example.SkillSharingEI1027.dao;

import com.example.SkillSharingEI1027.modelo.Request;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class RequestRowMapper implements RowMapper<Request>{
    @Override
    public Request mapRow(ResultSet rs, int rowNum) throws SQLException {
        Request request=new Request();
        request.setIdRequest(rs.getString("idRequest"));
        request.setStartDate(rs.getDate("startDate"));
        request.setEndDate(rs.getDate("endDate"));
        request.setDescription(rs.getString("description"));
        //request.setIdStudent(rs.getString("idStudent"));
        request.setIdSkill(rs.getString("idSkill"));
        return request;
    }
}
