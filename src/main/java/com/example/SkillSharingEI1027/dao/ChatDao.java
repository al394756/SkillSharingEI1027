package com.example.SkillSharingEI1027.dao;

import com.example.SkillSharingEI1027.modelo.Chat;
import com.example.SkillSharingEI1027.modelo.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class ChatDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){ jdbcTemplate= new JdbcTemplate(dataSource);}

    public void createChat(Student student1, Student student2){
        String id = idGenerator();
        jdbcTemplate.update("INSERT INTO Chat VALUES(?,?,?)",id,student1.getIdStudent(),student2.getIdStudent());
    }

    public List<Chat> getChatsDeStudent(Student student){
        try{
            return jdbcTemplate.query("SELECT * FROM Chat WHERE user1=? OR user2=?", new ChatRowMapper(), student.getIdStudent(),student.getIdStudent());
        } catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    private String idGenerator(){
        AtomicInteger contadorChats = new AtomicInteger(getCantidadChats());
        StringBuilder id = new StringBuilder("ch");
        int maximo0 = 6;
        int numeroId = contadorChats.get();
        int numeroCifras = Integer.toString(numeroId).length();
        id.append("0".repeat(Math.max(0,maximo0-numeroCifras)));
        return id.toString()+numeroId;
    }

    private Integer getCantidadChats(){
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM CHAT", new IntegerRowMapper());

    }
}
