package com.example.SkillSharingEI1027.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class StatisticsDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){ jdbcTemplate= new JdbcTemplate(dataSource);}

    public int numberOfDistinctSkillsInOffeRequest(String table){
        try{
            return jdbcTemplate.queryForObject("SELECT COUNT(DISTINCT idSkill) from "+table, Integer.class);
        } catch (EmptyResultDataAccessException e){
            return -1;
        }
    }

    public int numberOfDistinctSkills(){
        try{
            return jdbcTemplate.queryForObject("SELECT COUNT(DISTINCT idSkill) from skill", Integer.class);
        } catch (EmptyResultDataAccessException e){
            return -1;
        }
    }
}
