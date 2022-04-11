package com.example.SkillSharingEI1027.dao;

import com.example.SkillSharingEI1027.modelo.OffeRequest;
import com.example.SkillSharingEI1027.modelo.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class RequestDao extends OffeRequestDao{

    @Autowired
    public void setDataSource(DataSource dataSource){
        setJdbcTemplate(new JdbcTemplate(dataSource));
    }

    public List<OffeRequest> getRequests(){
        try{
            return getJdbcTemplate().query("SELECT * FROM Request", new OffeRequestRowMapper());
        } catch (EmptyResultDataAccessException e){
            return new ArrayList<>();
        }
    }

    public List<OffeRequest> getActiveOffeRequests(){
        try{
            return getJdbcTemplate().query("SELECT * FROM Request WHERE endDate>?", new OffeRequestRowMapper(),java.time.LocalDate.now());
        } catch (EmptyResultDataAccessException e){
            return new ArrayList<>();
        }
    }
}
