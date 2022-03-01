package com.example.SkillSharingEI1027.dao;

import com.example.SkillSharingEI1027.modelo.Skill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SkillDao {
    private JdbcTemplate jbdcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){ jbdcTemplate = new JdbcTemplate(dataSource);}

    //Añade nueva Skill a la base de datos
    public void addSkill(Skill skill){
        jbdcTemplate.update("INSERT INTO Skill VALUES(?,?,?,?)", skill.getIdSkill(), skill.getName(), skill.getDescription(), skill.getLevel());
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

    public List<Skill> getSkills(){
        try{
            return jbdcTemplate.query("SELECT * FROM Skill", new SkillRowMapper());
        } catch (EmptyResultDataAccessException e){
            return new ArrayList<>();
        }
    }

}
