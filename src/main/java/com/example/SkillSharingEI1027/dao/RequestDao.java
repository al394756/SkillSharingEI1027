package com.example.SkillSharingEI1027.dao;

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
public class RequestDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){ jdbcTemplate = new JdbcTemplate(dataSource);}

    public void addRequest(Request request){
        request.setIdRequest(idGenerator());
        System.out.println(request);
        jdbcTemplate.update("INSERT INTO Request VALUES(?,?,?,?,?,?)", request.getIdRequest(),request.getIdSkill(),
                request.getStartDate(), request.getEndDate(), request.getDescription(),request.getIdStudent());
    }

    public void deleteRequest(String idRequest){
        System.out.println("id "+idRequest);
        jdbcTemplate.update("UPDATE Request SET endDate=? WHERE idRequest=?", java.time.LocalDate.now(),idRequest);
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

    private String idGenerator(){
        AtomicInteger contadorRequests = new AtomicInteger(getCantidadRequests());
        StringBuilder id= new StringBuilder("rq");
        int numeroId = contadorRequests.get();
        int numeroCifras = Integer.toString(numeroId).length();
        id.append("0".repeat(Math.max(0, 6 - numeroCifras)));

        return id.toString() +numeroId;
    }

    private Integer getCantidadRequests(){
        return jdbcTemplate.queryForObject("SELECT COUNT(idSkill) FROM Request",new IntegerRowMapper());

    }
}
