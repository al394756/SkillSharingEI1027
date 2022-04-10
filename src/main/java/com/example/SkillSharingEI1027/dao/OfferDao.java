package com.example.SkillSharingEI1027.dao;

import com.example.SkillSharingEI1027.modelo.Collaboration;
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
public class OfferDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){ jdbcTemplate = new JdbcTemplate(dataSource);}

    public void addOffer(Offer offer){
        offer.setIdOffer(idGenerator());
        System.out.println(offer);
        jdbcTemplate.update("INSERT INTO Offer VALUES(?,?,?,?,?,?)", offer.getIdOffer(),
                offer.getStartDate(), offer.getEndDate(), offer.getDescription(),offer.getIdStudent(),offer.getIdSkill());
    }

    public void deleteOffer(String idOffer){
        System.out.println(idOffer);
        jdbcTemplate.update("UPDATE Offer SET endDate=? WHERE idOffer=?", java.time.LocalDate.now(),idOffer);
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

    public List<Offer> getActiveOffers(){
        try{
            return jdbcTemplate.query("SELECT * FROM Offer WHERE endDate>?", new OfferRowMapper(),java.time.LocalDate.now());
        } catch (EmptyResultDataAccessException e){
            return new ArrayList<>();
        }
    }

    private String idGenerator(){
        AtomicInteger contadorOffers = new AtomicInteger(getCantidadOffers());
        StringBuilder id= new StringBuilder("of");
        int numeroId = contadorOffers.get();
        int numeroCifras = Integer.toString(numeroId).length();
        id.append("0".repeat(Math.max(0, 6 - numeroCifras)));

        return id.toString() +numeroId;
    }

    private Integer getCantidadOffers(){
        return jdbcTemplate.queryForObject("SELECT COUNT(idSkill) FROM Request",new IntegerRowMapper());
    }
}
