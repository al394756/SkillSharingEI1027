package com.example.SkillSharingEI1027.dao;

import com.example.SkillSharingEI1027.modelo.OffeRequest;
import com.example.SkillSharingEI1027.modelo.Offer;
import com.example.SkillSharingEI1027.modelo.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public abstract class OffeRequestDao {
    private JdbcTemplate jdbcTemplate;

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate=jdbcTemplate;
    }

    @Autowired
    public void setDataSource(DataSource dataSource){ jdbcTemplate = new JdbcTemplate(dataSource);}

    public void add(OffeRequest offeRequest){
        String table="";
        if (offeRequest.getClass().equals(Request.class)){
            table="Request";
            offeRequest.setId(idGenerator("rq"));
        }

        else if (offeRequest.getClass().equals(Offer.class)){
            table="Offer";
            offeRequest.setId(idGenerator("of"));
        }

        jdbcTemplate.update("INSERT INTO "+table+" VALUES(?,?,?,?,?,?)", offeRequest.getId(),offeRequest.getSkill().getIdSkill(),
                offeRequest.getStartDate(), offeRequest.getEndDate(), offeRequest.getDescription(),offeRequest.getStudent().getIdStudent());
    }

    public void delete(String id){
        String table="";
        if (id.startsWith("rq")) table="Request";
        else if (id.startsWith("of")) table="Offer";
        jdbcTemplate.update("UPDATE "+table+" SET endDate=? WHERE id=?",java.time.LocalDate.now(),id);
    }

    public void update(OffeRequest offeRequest){
        String table="";
        if (offeRequest.getClass().equals(Request.class)) table="Request";
        else if (offeRequest.getClass().equals(Offer.class)) table="Offer";
        jdbcTemplate.update("UPDATE "+table+" SET description=?, startDate=?, endDate=? WHERE id=?",
                offeRequest.getDescription(),offeRequest.getStartDate(),offeRequest.getEndDate(),offeRequest.getId());
    }

    public OffeRequest getOffeRequest(String id){
        try{
            String table="";
            if (id.startsWith("rq")) table="Request";
            else if (id.startsWith("of")) table="Offer";
            return jdbcTemplate.queryForObject("SELECT * FROM "+table+" WHERE id=?",
                    new OffeRequestRowMapper(),id);
        } catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    private String idGenerator(String start){
        String table="";
        if (start.startsWith("rq")) table="Request";
        else if (start.startsWith("of")) table="Offer";
        AtomicInteger contadorRequests = new AtomicInteger(getCantidad(table));
        StringBuilder id= new StringBuilder(start);
        int numeroId = contadorRequests.get();
        int numeroCifras = Integer.toString(numeroId).length();
        id.append("0".repeat(Math.max(0, 6 - numeroCifras)));

        return id.toString() +numeroId;
    }

    private Integer getCantidad(String table){
        return jdbcTemplate.queryForObject("SELECT COUNT(id) FROM "+table,new IntegerRowMapper());
    }

    public abstract List<OffeRequest> getActiveOffeRequests();
}
