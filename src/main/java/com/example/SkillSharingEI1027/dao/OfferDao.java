package com.example.SkillSharingEI1027.dao;

import com.example.SkillSharingEI1027.modelo.Collaboration;
import com.example.SkillSharingEI1027.modelo.Offer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OfferDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){ jdbcTemplate = new JdbcTemplate(dataSource);}

    public void addOffer(Offer offer){
        jdbcTemplate.update("INSERT INTO Offer VALUES(?,?,?,?,?,?)", offer.getIdOffer(),offer.getIdSkill(),
                offer.getStartDate(), offer.getEndDate(), offer.getDescription(),offer.getIdStudent());
    }

    public void deleteOffer(String idOffer){
        jdbcTemplate.update("UPDATE Offer SET endDate=? WHERE idSOffer=?", java.time.LocalDate.now(),idOffer);
    }

    public void updateOffer(Offer offer){
        jdbcTemplate.update("UPDATE Offer SET description=?, startDate=?, endDate=? WHERE idOffer=?",
                offer.getDescription(),offer.getStartDate(),offer.getEndDate(),offer.getIdOffer());
    }

    public Offer getOffer(String idOffer){
        try{
            return jdbcTemplate.queryForObject("SELECT * FROM Offer WHERE idOffer=?",
                    new OfferRowMapper(),idOffer);
        } catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    public List<Offer> getOffers(){
        try{
            return jdbcTemplate.query("SELECT * FROM Offer", new OfferRowMapper());
        } catch (EmptyResultDataAccessException e){
            return new ArrayList<>();
        }
    }
}
