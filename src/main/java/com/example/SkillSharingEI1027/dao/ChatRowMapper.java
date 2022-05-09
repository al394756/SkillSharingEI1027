package com.example.SkillSharingEI1027.dao;

import com.example.SkillSharingEI1027.modelo.Chat;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class ChatRowMapper implements RowMapper {
    @Override
    public Chat mapRow(ResultSet rs, int rowNum) throws SQLException {
       Chat chat = new Chat();
       chat.setIdChat(rs.getString("idchat"));

       chat.setUser1(rs.getString("user1"));
       chat.setUser2(rs.getString("user2"));
       chat.setNewMsgParaStudent1(rs.getBoolean("newMsgParaStudent1"));
       chat.setNewMsgParaStudent2(rs.getBoolean("newMsgParaStudent2"));

        return  chat;
    }
}
