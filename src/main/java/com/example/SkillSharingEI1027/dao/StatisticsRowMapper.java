package com.example.SkillSharingEI1027.dao;

import com.example.SkillSharingEI1027.modelo.Statistics;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StatisticsRowMapper implements RowMapper<Statistics> {
    @Override
    public Statistics mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Statistics(rs.getString(1),rs.getInt(2));
    }
}