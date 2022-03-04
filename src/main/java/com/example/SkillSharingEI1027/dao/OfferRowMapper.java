package com.example.SkillSharingEI1027.dao;

import com.example.SkillSharingEI1027.modelo.Offer;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class OfferRowMapper implements RowMapper<Offer>{
    @Override
    public Offer mapRow(ResultSet rs, int rowNum) throws SQLException {
        Offer offer=new Offer();
        offer.setIdOffer(rs.getString("idOffer"));
        offer.setStartDate(rs.getDate("startDate"));
        offer.setEndDate(rs.getDate("endDate"));
        offer.setDescription(rs.getString("description"));
        offer.setIdStudent(rs.getString("idStudent"));
        offer.setIdSkill(rs.getString("idSkill"));
        return offer;
    }
}
