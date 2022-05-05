package com.example.SkillSharingEI1027.dao;

import com.example.SkillSharingEI1027.modelo.Collaboration;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class CollaborationRowMapper implements RowMapper<Collaboration> {
    @Override
    public Collaboration mapRow(ResultSet rs, int rowNum) throws SQLException {
        Collaboration collaboration = new Collaboration();
        collaboration.setIdCollaboration(rs.getString("idCollaboration"));
        collaboration.setAssessmentScore(rs.getInt("AssessmentScore"));
        collaboration.setCollaborationState(rs.getInt("CollaborationState"));
        collaboration.getIdOffer().setId(rs.getString("idOffer"));
        collaboration.getIdRequest().setId(rs.getString("idRequest"));
        collaboration.setHours(rs.getInt("hours"));
        return collaboration;
    }
}
