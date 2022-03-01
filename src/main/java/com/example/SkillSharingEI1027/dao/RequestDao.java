package com.example.SkillSharingEI1027.dao;

import com.example.SkillSharingEI1027.modelo.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RequestDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){ jdbcTemplate = new JdbcTemplate(dataSource);}

    public void addRequest(Request request){
        jdbcTemplate.update("INSERT INTO Request VALUES(?,?,?,?,?,?)", request.getIdRequest(),request.getIdSkill(),
                request.getStartDate(), request.getEndDate(), request.getDescription(),request.getIdStudent());
    }

    public void deleteRequest(String idRequest){
        jdbcTemplate.update("UPDATE Request SET idEndDate=? WHERE idSRequest=?", java.time.LocalDate.now(),idRequest);
    }

    public void updateRequest(Request request){
        jdbcTemplate.update("UPDATE Request SET description=?, startDate=?, endDate=? WHERE idRequest=?",
                request.getDescription(),request.getStartDate(),request.getEndDate(),request.getIdRequest());
    }

    public Request getRequest(String idRequest){
        try{
            return jdbcTemplate.queryForObject("SELECT * FROM Request WHERE idRequest=?",
                    new RequestRowMapper(),idRequest);
        } catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    public List<Request> getRequests(){
        try{
            return jdbcTemplate.query("SELECT * FROM Request", new RequestRowMapper());
        } catch (EmptyResultDataAccessException e){
            return new ArrayList<>();
        }
    }
}
