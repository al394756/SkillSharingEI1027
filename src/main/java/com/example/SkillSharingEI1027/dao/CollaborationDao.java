package com.example.SkillSharingEI1027.dao;

import com.example.SkillSharingEI1027.modelo.Collaboration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class CollaborationDao {
    private JdbcTemplate jdbcTemplate;
    private AtomicInteger idAtomic = new AtomicInteger(1);

    @Autowired
    public void setDataSource(DataSource dataSource){ jdbcTemplate = new JdbcTemplate(dataSource);}

    public void addCollaboration(Collaboration collab){
        collab.setIdCollaboration(idGenerator());
        jdbcTemplate.update("INSERT INTO Collaboration VALUES(?,?,?,?,?,?)",
                collab.getIdCollaboration(),collab.getAssessmentScore(), collab.isCollaborationState(),
                collab.getIdOffer(), collab.getIdRequest(), collab.getHours());
    }
    private String idGenerator(){
        Integer contador = idAtomic.getAndIncrement();
        StringBuilder id= new StringBuilder("co");
        int numeroCifras = Integer.toString(contador).length();
        id.append("0".repeat(Math.max(0, 6 - numeroCifras)));
        return id.toString() + contador;
    }

    public void deleteCollaboration(Collaboration collab){
        jdbcTemplate.update("DELETE FROM Collaboration WHERE idCollaboration =?",collab.getIdCollaboration());
    }

    public void deleteCollaboration(String collab){
        jdbcTemplate.update("DELETE FROM Collaboration WHERE idCollaboration =?", collab);
    }

    public void updateCollaboration(Collaboration collab){
        jdbcTemplate.update("UPDATE Collaboration SET assessmentScore=?, collaborationState=?, idOffer=?, " +
                        "idRequest=?, hours=? WHERE idCollaboration=?", collab.getAssessmentScore(),
                collab.isCollaborationState(),collab.getIdOffer(),collab.getIdRequest(),collab.getHours());
    }

    public Collaboration getCollaboration(String idCollaboration){
        try{
            return jdbcTemplate.queryForObject("SELECT * FROM Collaboration WHERE idCollaboration=?",
                    new CollaborationRowMapper(),idCollaboration);
        } catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    public List<Collaboration> getCollaborations(){
        try{
            return jdbcTemplate.query("SELECT * FROM Collaboration", new CollaborationRowMapper());
        } catch (EmptyResultDataAccessException e){
            return new ArrayList<>();
        }
    }




}
