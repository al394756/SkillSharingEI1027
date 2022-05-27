package com.example.SkillSharingEI1027.dao;

import com.example.SkillSharingEI1027.modelo.Collaboration;
import com.example.SkillSharingEI1027.modelo.OffeRequest;
import com.example.SkillSharingEI1027.modelo.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class OffeRequestDao {
    private JdbcTemplate jdbcTemplate;
    private String fromSentenceR = "FROM REQUEST AS R INNER JOIN STUDENT AS S ON S.IDSTUDENT = R.IDSTUDENT INNER JOIN SKILL AS SK ON SK.IDSKILL=R.IDSKILL";
    private String fromSentenceO = "FROM Offer AS o INNER JOIN STUDENT AS S ON S.IDSTUDENT = o.IDSTUDENT INNER JOIN SKILL AS SK ON SK.IDSKILL=o.IDSKILL";


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

            if (id.startsWith("rq")) {
                return jdbcTemplate.queryForObject("SELECT * " + fromSentenceR + " WHERE id=?",
                        new OffeRequestRowMapper("Request"), id);
            }
            else {
                return jdbcTemplate.queryForObject("SELECT * " + fromSentenceO + " WHERE id=?",
                        new OffeRequestRowMapper("Offer"), id);
            }

        } catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    private String idGenerator(OffeRequest offeRequest){
        AtomicInteger contadorRequests = new AtomicInteger(getCantidad(offeRequest.getType()));
        int numeroId = contadorRequests.get();
        int numeroCifras = Integer.toString(numeroId).length();
        return offeRequest.getStart() + "0".repeat(Math.max(0, 6 - numeroCifras)) + numeroId;
    }

    private int getCantidad(String table){
        return jdbcTemplate.queryForObject("SELECT COUNT(id) FROM "+table,Integer.class);
    }

    public List<OffeRequest> getActiveOffeRequests(String table) {
        try{
            if (table.equals("Request"))
                return jdbcTemplate.query("SELECT * "+fromSentenceR+" WHERE endDate>?", new OffeRequestRowMapper(table),java.time.LocalDate.now());
            else
                return jdbcTemplate.query("SELECT * "+fromSentenceO+" WHERE endDate>?", new OffeRequestRowMapper(table),java.time.LocalDate.now());

        } catch (EmptyResultDataAccessException e){
            return new ArrayList<>();
        }
    }

    public List<OffeRequest> getOffeRequests(String table){
        try{
            if (table.equals("Request"))
                return jdbcTemplate.query("SELECT * "+fromSentenceR, new OffeRequestRowMapper(table));
            return jdbcTemplate.query("SELECT * "+fromSentenceO, new OffeRequestRowMapper(table));

        } catch (EmptyResultDataAccessException e){
            return new ArrayList<>();
        }
    }

    public List<OffeRequest> getOfferRequestsActivasDe(String table, Student student){
        try{
            if (table.equals("Request"))
                return jdbcTemplate.query("SELECT * "+fromSentenceR+" WHERE endDate>? AND idStudent=?", new OffeRequestRowMapper(table), java.time.LocalDate.now(), student.getIdStudent());
            return jdbcTemplate.query("SELECT * "+fromSentenceO+" WHERE endDate>? AND idStudent=?", new OffeRequestRowMapper(table), java.time.LocalDate.now(), student.getIdStudent());

        } catch (EmptyResultDataAccessException e){
            return new ArrayList<>();
        }
    }

    public List<OffeRequest> getOffeRequestWithSkill(String table, String idskill, LocalDate date, Student student){
        try{
            if (table.equals("Request"))
                return jdbcTemplate.query("SELECT * "+fromSentenceR+" WHERE r.idSkill=? AND r.endDate>? AND r.idStudent != ?", new OffeRequestRowMapper(table), idskill,date, student.getIdStudent());
            return jdbcTemplate.query("SELECT * "+fromSentenceO+" WHERE o.idSkill=? AND o.endDate>? AND o.idStudent != ?", new OffeRequestRowMapper(table), idskill,date, student.getIdStudent());

        } catch (EmptyResultDataAccessException e){
            return new ArrayList<>();
        }
    }


}
