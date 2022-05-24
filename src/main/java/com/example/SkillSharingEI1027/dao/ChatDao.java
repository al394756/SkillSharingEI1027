package com.example.SkillSharingEI1027.dao;

import com.example.SkillSharingEI1027.modelo.Chat;
import com.example.SkillSharingEI1027.modelo.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class ChatDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){ jdbcTemplate= new JdbcTemplate(dataSource);}

    public String createChat(Student student1, Student student2){
        String id = idGenerator();
        jdbcTemplate.update("INSERT INTO Chat VALUES(?,?,?,?,?)",id,student1.getIdStudent(),student2.getIdStudent(),false,false);
        return id;
    }

    public List<Chat> getChatsDeStudent(Student student){
        try{
            return jdbcTemplate.query("SELECT * FROM Chat WHERE user1=? OR user2=?", new ChatRowMapper(), student.getIdStudent(),student.getIdStudent());
        } catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public List<Chat> getChatsDeStudentSinLeer(Student student){
        try{
            return jdbcTemplate.query("SELECT * FROM Chat WHERE (user1=? AND newMsgParaStudent1=true) OR (user2=? AND newMsgParaStudent2=true)", new ChatRowMapper(), student.getIdStudent(),student.getIdStudent());
        } catch (EmptyResultDataAccessException e){
            return null;
        }
    }


    public List<Chat> getChatsDeStudentLeidos(Student student){
        try{
            return jdbcTemplate.query("SELECT * FROM Chat WHERE (user1=? AND newMsgParaStudent1=false) OR (user2=? AND newMsgParaStudent2=false)", new ChatRowMapper(), student.getIdStudent(),student.getIdStudent());
        } catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public void updateChat(Chat chat){
        jdbcTemplate.update("UPDATE CHAT SET newMsgParaStudent1=?, newMsgParaStudent2=? WHERE idChat=?", chat.isNewMsgParaStudent1(),chat.isNewMsgParaStudent2(),chat.getIdChat());

    }
    public Chat getChatEntreStudents(Student student1, Student student2){
        try{
            return (Chat) jdbcTemplate.queryForObject("SELECT * FROM Chat WHERE (user1=? AND user2=?) OR (user1=? AND user2=?)", new ChatRowMapper(), student1.getIdStudent(),student2.getIdStudent(), student2.getIdStudent(),student1.getIdStudent());
        } catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public List<String> getStudents(Chat chat){
        List<String> list=new LinkedList<>();
        try{
            Chat chat1  = getChatConId(chat.getIdChat());
            list.add(chat1.getUser1());
            list.add(chat1.getUser2());
            return list;

        } catch (EmptyResultDataAccessException e){
            return null;
        }
    }
    private String idGenerator(){
        AtomicInteger contadorChats = new AtomicInteger(getCantidadChats());
        int maximo0 = 6;
        int numeroId = contadorChats.get();
        int numeroCifras = Integer.toString(numeroId).length();
        return "ch"+"0".repeat(Math.max(0, maximo0 - numeroCifras)) +numeroId;
    }

    private int getCantidadChats(){
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM CHAT", Integer.class);

    }

    public Chat getChatConId(String id){
        try{
            return (Chat) jdbcTemplate.queryForObject("SELECT * FROM Chat WHERE idChat=?", new ChatRowMapper(), id);
        } catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public int getCantidadChatsSinLeer(Student student){
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM CHAT WHERE (user1=? AND newMsgParaStudent1=true) OR (user2=? AND newMsgParaStudent2=true)", Integer.class,student.getIdStudent(),student.getIdStudent());

    }
}
