/*package com.example.SkillSharingEI1027.dao;

import com.example.SkillSharingEI1027.modelo.Offer;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public final class OfferRowMapper implements RowMapper<Offer>{
    @Override
    public Offer mapRow(ResultSet rs, int rowNum) throws SQLException {
        Offer offer=new Offer();
        offer.setId(rs.getString("idOffer"));
        offer.setStartDate(rs.getObject("startDate", LocalDate.class));
        offer.setEndDate(rs.getObject("endDate",LocalDate.class));
        offer.setDescription(rs.getString("description"));
        offer.setIdStudent(rs.getString("idStudent"));
        offer.setIdSkill(rs.getString("idSkill"));
        return offer;
    }
}*/
