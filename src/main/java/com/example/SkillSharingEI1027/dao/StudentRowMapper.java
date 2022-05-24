package com.example.SkillSharingEI1027.dao;

import com.example.SkillSharingEI1027.modelo.Student;
import org.springframework.jdbc.core.RowMapper;


import java.sql.ResultSet;
import java.sql.SQLException;

public final class StudentRowMapper implements RowMapper<Student> {

    @Override
    public Student mapRow(ResultSet rs, int rowNum) throws SQLException{
        Student student= new Student();
        student.setIdStudent(rs.getString("idStudent"));
        student.setDni(rs.getString("dni"));
        student.setName(rs.getString("name"));
        student.setEmail(rs.getString("email"));
        student.setPhoneNumber(rs.getInt("phoneNumber"));
        student.setPassword(rs.getString("password"));
        student.setDegree(rs.getString("degree"));
        student.setCourse(rs.getInt("course"));
        student.setBalanceHours(rs.getInt("balanceHours"));
        student.setSkpMember(rs.getBoolean("skpMember"));
        student.setActiveAccount(rs.getBoolean("activeAccount"));
        student.setBanReason(rs.getString("banReason"));
        return student;
    }
}
