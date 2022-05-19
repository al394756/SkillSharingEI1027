package com.example.SkillSharingEI1027.dao;

import com.example.SkillSharingEI1027.modelo.Skill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class SkillDao {
    private JdbcTemplate jbdcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){ jbdcTemplate = new JdbcTemplate(dataSource);}

    //Añade nueva Skill a la base de datos
    public void addSkill(Skill skill){
        jbdcTemplate.update("INSERT INTO Skill VALUES(?,?,?,?)", idGenerator(), skill.getName(), skill.getDescription(), skill.getLevel());
    }

    //Elimina una skill de la base de datos
    public void deleteSkill(String idSkill){
        jbdcTemplate.update("DELETE FROM Skill WHERE idSkill=?", idSkill);
    }

    //Actualiza la dscripción de la Skill
    public void updateSkill(Skill skill){
        jbdcTemplate.update("UPDATE Skill SET description=? WHERE idSkill=?", skill.getDescription(),skill.getIdSkill());
    }

    public Skill getSkill(String idSkill){
        try{
            return jbdcTemplate.queryForObject("SELECT * FROM Skill WHERE idSkill=?", new SkillRowMapper(),idSkill);
        } catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    public Skill getIdBySkill(String skill){
        try{
            return jbdcTemplate.queryForObject("SELECT * FROM Skill WHERE name=?", new SkillRowMapper(),skill);
        } catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    public List<Skill> getSkills(){
        try{
            return jbdcTemplate.query("SELECT * FROM Skill", new SkillRowMapper());
        } catch (EmptyResultDataAccessException e){
            return new ArrayList<>();
        }
    }

    public List<String> getSkillsName(){
        try{
            List<String> skills=new ArrayList<>();
            for (Skill skill:getSkills()) skills.add(skill.getName());
            return skills;
        } catch (EmptyResultDataAccessException e){
            return new ArrayList<>();
        }
    }

    private int getCantidadSkills(){
        return jbdcTemplate.queryForObject("SELECT COUNT(name) FROM Skill",Integer.class);

    }
    //Genera un id de formato "sk000000", sk + 6 cifras (permite tener hasta 1000000 usuarios registrados)
    private String idGenerator(){
        AtomicInteger contadorStudents = new AtomicInteger(getCantidadSkills());
        StringBuilder id= new StringBuilder("sk");
        int maximo0 = 6;
        int numeroId = contadorStudents.get();
        int numeroCifras = Integer.toString(numeroId).length();
        id.append("0".repeat(Math.max(0, maximo0 - numeroCifras)));

        return id.toString() +numeroId;
    }
}
