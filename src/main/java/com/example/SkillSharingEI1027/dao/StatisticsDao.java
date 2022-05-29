package com.example.SkillSharingEI1027.dao;

import com.example.SkillSharingEI1027.modelo.Statistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class StatisticsDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){ jdbcTemplate= new JdbcTemplate(dataSource);}

    public int numberOfDistinctSkillsInOffeRequest(String table){
        try{
            return jdbcTemplate.queryForObject("SELECT cast(COUNT(DISTINCT idSkill) as float)/cast((SELECT COUNT(DISTINCT idSkill) from skill) as float)*100 from "+table+" where endDate>CURRENT_DATE", Integer.class);
        } catch (EmptyResultDataAccessException e){
            return -1;
        }
    }

    public int numberOfDistinctSkillsInCollaboration(){
        try{
            return jdbcTemplate.queryForObject("select cast(COUNT(DISTINCT idSkill) as float)/cast((SELECT COUNT(DISTINCT idSkill) from skill) as float)*100 from collaboration as c inner join offer as o on c.idOffer=o.id where o.endDate>CURRENT_DATE", Integer.class);
        } catch (EmptyResultDataAccessException e){
            return -1;
        }
    }

    public List<Statistics> timesOfSkillUsedOffeRequest(String table){
        try{
            return jdbcTemplate.query("select s.name, (cast(count(o.idSkill)as float)*cast(100 as float)/cast((select count(*) from "+table+" where enddate>CURRENT_DATE) as float)) as percentage from "+table+" as o join skill as s using(idSkill) where endDate>CURRENT_DATE group by s.name order by percentage DESC fetch first 3 ROWS ONLY",new StatisticsRowMapper());
        } catch (EmptyResultDataAccessException e){
            return new ArrayList<>();
        }
    }

    public List<Statistics> timesOfSkillUsedCollaboration(){
        try{
            return jdbcTemplate.query("select s.name, (cast(count(o.idSkill) as float ) / cast((select count (*) from collaboration where collaborationstate=0 or collaborationstate=1) as float )*100) as percentage from collaboration as c inner join offer as o on c.idOffer=o.id join skill as s using(idSkill)  where c.collaborationstate=0 or c.collaborationstate=1 group by s.name order by percentage DESC fetch first 3 ROWS ONLY",new StatisticsRowMapper());
        } catch (EmptyResultDataAccessException e){
            return new ArrayList<>();
        }
    }

    public List<Statistics> timesMVPStudentOffeRequest(String table){
        try{
            return jdbcTemplate.query("select s.name, (count(o.idStudent)*cast(100 as float)/cast((select count(*) from "+table+" where enddate>CURRENT_DATE) as float)) as percentage from "+table+" as o join student as s using(idStudent) where endDate>CURRENT_DATE group by s.name order by percentage DESC fetch first 3 ROWS ONLY ",new StatisticsRowMapper());
        } catch (EmptyResultDataAccessException e){
            return new ArrayList<>();
        }
    }
    public List<Statistics> timesMVPStudentCollaboration(){
        try{
            return jdbcTemplate.query("select s.name, (cast(count(o.idStudent) as float )/ cast((select count (*) from collaboration where collaborationstate=0 or collaborationstate=1) as float )*100) as percentage from collaboration as c inner join offer as o on c.idOffer=o.id join student as s using(idStudent) where c.collaborationstate=0 or c.collaborationstate=1 group by s.name order by percentage DESC fetch first 3 ROWS ONLY",new StatisticsRowMapper());
        } catch (EmptyResultDataAccessException e){
            return new ArrayList<>();
        }
    }
}
