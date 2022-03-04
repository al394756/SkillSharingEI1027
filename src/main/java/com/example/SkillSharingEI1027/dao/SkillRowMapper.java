package com.example.SkillSharingEI1027.dao;

import org.springframework.jdbc.core.RowMapper;

import com.example.SkillSharingEI1027.modelo.Skill;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class SkillRowMapper implements RowMapper<Skill>{


    @Override
    public Skill mapRow(ResultSet rs, int rowNum) throws SQLException {
        Skill skill= new Skill();
        skill.setIdSkill(rs.getString("idSkill"));
        skill.setName(rs.getString("name"));
        skill.setDescription(rs.getString("description"));
        skill.setLevel(rs.getInt("skillLevel"));
        return skill;
    }
}
