package com.example.SkillSharingEI1027.dao;

import com.example.SkillSharingEI1027.modelo.Collaboration;
import com.example.SkillSharingEI1027.modelo.OffeRequest;
import com.example.SkillSharingEI1027.modelo.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class OffeRequestDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){ jdbcTemplate = new JdbcTemplate(dataSource);}

    public OffeRequest add(OffeRequest offeRequest){
        offeRequest.setId(idGenerator(offeRequest));
        jdbcTemplate.update("INSERT INTO "+offeRequest.getType()+" VALUES(?,?,?,?,?,?)", offeRequest.getId(),offeRequest.getSkill().getIdSkill(),
                offeRequest.getStartDate(), offeRequest.getEndDate(), offeRequest.getDescription(),offeRequest.getStudent().getIdStudent());
        return offeRequest;
    }

    public void delete(String id){
        String table="";
        if (id.startsWith("rq")) table="Request";
        else if (id.startsWith("of")) table="Offer";
        jdbcTemplate.update("UPDATE "+table+" SET endDate=? WHERE id=?",java.time.LocalDate.now(),id);
    }

    public void update(OffeRequest offeRequest){
        jdbcTemplate.update("UPDATE "+offeRequest.getType()+" SET description=?, startDate=?, endDate=? WHERE id=?",
                offeRequest.getDescription(),offeRequest.getStartDate(),offeRequest.getEndDate(),offeRequest.getId());
    }

    public OffeRequest getOffeRequest(String id){
        try{
            String table="";
            if (id.startsWith("rq")) table="Request";
            else if (id.startsWith("of")) table="Offer";
            return jdbcTemplate.queryForObject("SELECT * FROM "+table+" WHERE id=?",
                    new OffeRequestRowMapper(table),id);
        } catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    private String idGenerator(OffeRequest offeRequest){
        AtomicInteger contadorRequests = new AtomicInteger(getCantidad(offeRequest.getType()));
        StringBuilder id= new StringBuilder(offeRequest.getStart());
        int numeroId = contadorRequests.get();
        int numeroCifras = Integer.toString(numeroId).length();
        id.append("0".repeat(Math.max(0, 6 - numeroCifras)));
        return id.toString() +numeroId;
    }

    private Integer getCantidad(String table){
        return jdbcTemplate.queryForObject("SELECT COUNT(id) FROM "+table,new IntegerRowMapper());
    }

    public List<OffeRequest> getActiveOffeRequests(String table) {
        try{
            return jdbcTemplate.query("SELECT * FROM "+table+" WHERE endDate>?", new OffeRequestRowMapper(table),java.time.LocalDate.now());
        } catch (EmptyResultDataAccessException e){
            return new ArrayList<>();
        }
    }

    public List<OffeRequest> getOffeRequests(String table){
        try{
            return jdbcTemplate.query("SELECT * FROM "+table, new OffeRequestRowMapper(table));
        } catch (EmptyResultDataAccessException e){
            return new ArrayList<>();
        }
    }

    public List<OffeRequest> getOfferRequestsActivasDe(String table, Student student){
        try{
            return jdbcTemplate.query("SELECT * FROM "+table+" WHERE endDate>? AND idStudent=?", new OffeRequestRowMapper(table), java.time.LocalDate.now(), student.getIdStudent());
        } catch (EmptyResultDataAccessException e){
            return new ArrayList<>();
        }
    }

    public List<OffeRequest> getOffeRequestWithSkill(String table, String idskill){
        try{
            return jdbcTemplate.query("SELECT * FROM "+table+" WHERE idSkill=?", new OffeRequestRowMapper(table), idskill);
        } catch (EmptyResultDataAccessException e){
            return new ArrayList<>();
        }
    }

    public List<Collaboration> getOffersPorAceptar(Student student){
        try{

            return jdbcTemplate.query("SELECT * FROM Collaboration c WHERE collaborationstate=0 and ? IN (SELECT idStudent FROM Offer o WHERE c.idOffer = o.id)", new CollaborationRowMapper(), student.getIdStudent());
        } catch (EmptyResultDataAccessException e){
            return new ArrayList<>();
        }
    }

    public List<Collaboration> getRequestsPorAceptar(Student student){
        try{

            return jdbcTemplate.query("SELECT * FROM Collaboration c WHERE collaborationstate=0 and ? IN (SELECT idStudent FROM Request r WHERE c.idRequest = r.id)", new CollaborationRowMapper(), student.getIdStudent());
        } catch (EmptyResultDataAccessException e){
            return new ArrayList<>();
        }
    }
}
