package com.example.SkillSharingEI1027.dao;

import com.example.SkillSharingEI1027.modelo.Collaboration;
import com.example.SkillSharingEI1027.modelo.OffeRequest;
import com.example.SkillSharingEI1027.modelo.Offer;
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
public class OfferDao extends OffeRequestDao{
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){ jdbcTemplate = new JdbcTemplate(dataSource);}

    @Override
    public List<OffeRequest> getActiveOffeRequests() {
        try{
            return jdbcTemplate.query("SELECT * FROM Offer WHERE endDate>?", new OffeRequestRowMapper(),java.time.LocalDate.now());
        } catch (EmptyResultDataAccessException e){
            return new ArrayList<>();
        }
    }

    public List<OffeRequest> getOffers(){
        try{
            return jdbcTemplate.query("SELECT * FROM Offer", new OffeRequestRowMapper());
        } catch (EmptyResultDataAccessException e){
            return new ArrayList<>();
        }
    }

    private Integer getCantidadOffers(){
        return jdbcTemplate.queryForObject("SELECT COUNT(id) FROM Offer",new IntegerRowMapper());
    }
}
