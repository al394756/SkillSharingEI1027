package com.example.SkillSharingEI1027.dao;

import com.example.SkillSharingEI1027.modelo.Collaboration;
import com.example.SkillSharingEI1027.modelo.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class CollaborationDao {
    private JdbcTemplate jdbcTemplate;
    private String fromSentence = "FROM COLLABORATION AS C inner JOIN OFFER AS O on c.idOffer=o.id INNER JOIN REQUEST AS R ON r.id = c.idRequest INNER JOIN Skill AS s on s.idSkill=r.idSkill INNER JOIN Student as st ON st.idStudent = r.idStudent INNER JOIN Student as st1 on st1.idStudent = o.idStudent";

    private AtomicInteger idAtomic = new AtomicInteger(1);

    @Autowired
    public void setDataSource(DataSource dataSource){ jdbcTemplate = new JdbcTemplate(dataSource);}

    public void addCollaboration(Collaboration collab){
        collab.setIdCollaboration(idGenerator());
        jdbcTemplate.update("INSERT INTO Collaboration VALUES(?,?,?,?,?,?,?)",
                collab.getIdCollaboration(),collab.getAssessmentScore(), collab.getCollaborationState(),
                collab.getIdOffer().getId(), collab.getIdRequest().getId(), collab.getHours(), collab.isRequestinicia());
    }
    private String idGenerator(){
        AtomicInteger contador = new AtomicInteger(getCantidadCollaborations());
        int numeroId = contador.get();
        int numeroCifras = Integer.toString(numeroId).length();
        return "co"+"0".repeat(Math.max(0, 6 - numeroCifras)) + numeroId;
    }
    private int getCantidadCollaborations(){
        return jdbcTemplate.queryForObject("SELECT COUNT(idCollaboration) FROM Collaboration",Integer.class);
    }

    public List<Collaboration> getCollaborationsActivas(){
        try{
            return jdbcTemplate.query("SELECT * "+fromSentence+" WHERE (collaborationstate=0 or collaborationstate=1 or collaborationState=2)", new CollaborationRowMapper());
        } catch (EmptyResultDataAccessException e){
            return new ArrayList<>();
        }
    }

    public List<Collaboration> getCollaborationsAcabadas(){
        try{
            return jdbcTemplate.query("SELECT * "+fromSentence+" WHERE ( collaborationstate=3 or collaborationstate=4)", new CollaborationRowMapper());
        } catch (EmptyResultDataAccessException e){
            return new ArrayList<>();
        }
    }

    public Collaboration getCollaboration(String idCollaboration){
        try{
            return jdbcTemplate.queryForObject("SELECT * "+fromSentence+" WHERE idCollaboration=?",
                    new CollaborationRowMapper(),idCollaboration);
        } catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    public List<Collaboration> getCollaborations(){
        try{
            return jdbcTemplate.query("SELECT * "+fromSentence, new CollaborationRowMapper());
        } catch (EmptyResultDataAccessException e){
            return new ArrayList<>();
        }
    }



    public List<Collaboration> getCollaborationsActivasDe(Student student){
        try{
            return jdbcTemplate.query("SELECT * "+fromSentence+" WHERE (collaborationstate=0 or collaborationstate=1 or collaborationState=2) and (? IN (SELECT idStudent FROM Request r WHERE c.idRequest = r.id) or ? IN (SELECT idStudent FROM Offer o WHERE c.idOffer = o.id))", new CollaborationRowMapper(), student.getIdStudent(),student.getIdStudent());
        } catch (EmptyResultDataAccessException e){
            return new ArrayList<>();
        }
    }

    public List<Collaboration> getCollaborationsAcabadasDe(Student student){
        try{
            return jdbcTemplate.query("SELECT * "+fromSentence+" WHERE ( collaborationstate=3 or collaborationstate=4) and (? IN (SELECT idStudent FROM Request r WHERE c.idRequest = r.id) or ? IN (SELECT idStudent FROM Offer o WHERE c.idOffer = o.id))", new CollaborationRowMapper(), student.getIdStudent(),student.getIdStudent());
        } catch (EmptyResultDataAccessException e){
            return new ArrayList<>();
        }
    }

    public void finalizeCollaboration(Collaboration collaboration){
        jdbcTemplate.update("UPDATE Collaboration SET collaborationState=3 WHERE idCollaboration=?",
                collaboration.getIdCollaboration());
    }
    public void confirmCollaboration(Collaboration collaboration){
        jdbcTemplate.update("UPDATE Collaboration SET collaborationState=1 WHERE idCollaboration=?",
                collaboration.getIdCollaboration());
    }

    public void cancelCollaboration(Collaboration collaboration){
        jdbcTemplate.update("UPDATE Collaboration SET collaborationState=4, hours=0 WHERE idCollaboration=?",
                collaboration.getIdCollaboration());
    }

    public void assessCollaboration(Collaboration collaboration){
        jdbcTemplate.update("UPDATE Collaboration SET collaborationState=3, hours=?, assessmentScore=? WHERE idCollaboration=?", collaboration.getHours(), collaboration.getAssessmentScore(), collaboration.getIdCollaboration());
    }

    public List<Collaboration> getCollaborationsDe(Student student){
        try{
            return jdbcTemplate.query("SELECT * "+fromSentence+" WHERE ? IN (SELECT idStudent FROM Request r WHERE c.idRequest = r.id) or ? IN (SELECT idStudent FROM Offer o WHERE c.idOffer = o.id)", new CollaborationRowMapper(), student.getIdStudent(),student.getIdStudent());
        } catch (EmptyResultDataAccessException e){
            return new ArrayList<>();
        }
    }

    public void actualizaCollaborations() {
        if (getCantidadCollaborations() > 0) {
            jdbcTemplate.update("UPDATE COLLABORATION SET collaborationState=2 WHERE collaborationState=1 AND idRequest IN (SELECT r.id FROM request as r WHERE CURRENT_DATE >= r.startDate) ");
            jdbcTemplate.update("UPDATE COLLABORATION SET collaborationState=4 WHERE collaborationState=0 AND idRequest IN (SELECT r.id from request as r WHERE CURRENT_DATE >= r.endDate)");
        }
    }
}
