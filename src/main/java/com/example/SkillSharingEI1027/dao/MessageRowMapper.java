package com.example.SkillSharingEI1027.dao;

import com.example.SkillSharingEI1027.modelo.Message;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public final class MessageRowMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        Message message = new Message();
        message.setIdChat(rs.getString("idchat"));
        message.setNumber(rs.getInt("number"));
        message.setStudent(rs.getString("idStudent"));
        message.setContent(rs.getString("content"));
        message.setDate(rs.getObject("day", LocalDate.class));
        return message;
    }
}
