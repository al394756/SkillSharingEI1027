package com.example.SkillSharingEI1027.dao;

import com.example.SkillSharingEI1027.modelo.Chat;
import com.example.SkillSharingEI1027.modelo.Message;
import com.example.SkillSharingEI1027.modelo.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.List;

@Repository
public class MessageDao {
    private JdbcTemplate jdbcTemplate;
    private Chat chat;


    @Autowired
    public void setDataSource(DataSource dataSource){ jdbcTemplate= new JdbcTemplate(dataSource);}

    public void addMessage(Message msg){
        jdbcTemplate.update("INSERT INTO Message VALUES(?,?,?,?,?)",msg.getIdChat(),msg.getNumber(),msg.getStudent(),msg.getContent(),LocalDate.now());

    }

    public void banMessage(String chat , Student student){
        jdbcTemplate.update("INSERT INTO Message VALUES(?,?,?,?,?)",chat,0,"id000000",student.getBanReason(), LocalDate.now());


    }

    public void officialMessage(String chat , Student student, String msg){
        jdbcTemplate.update("INSERT INTO Message VALUES(?,?,?,?,?)",chat,0,"id000000",msg, LocalDate.now());


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

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public Chat getChat() {
        return chat;
    }
}
