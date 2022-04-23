package com.example.SkillSharingEI1027.dao;

import com.example.SkillSharingEI1027.modelo.Chat;
import com.example.SkillSharingEI1027.modelo.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class MessageDao {
    private JdbcTemplate jdbcTemplate;
    private String chat;


    @Autowired
    public void setDataSource(DataSource dataSource){ jdbcTemplate= new JdbcTemplate(dataSource);}

    public void addMessage(Message msg){
        jdbcTemplate.update("INSERT INTO Message VALUES(?,?,?,?,?)",msg.getIdChat(),msg.getNumber(),msg.getStudent(),msg.getContent(),msg.getDate());

    }

    public List<Message> getMessagesFromChat(String idChat){
        try{
            return jdbcTemplate.query("SELECT * FROM Message WHERE idChat=?", new MessageRowMapper(), idChat);
        } catch (EmptyResultDataAccessException e){
            return null;
        }
    }
    public Integer messageNumber(String idChat){
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM Message WHERE idChat=?", new IntegerRowMapper(),idChat);

    }

    public void setChat(String chat) {
        this.chat = chat;
    }

    public String getChat() {
        return chat;
    }
}
