package com.example.SkillSharingEI1027.dao;

import com.example.SkillSharingEI1027.modelo.Student;

import java.util.Collection;

public interface UserDao {
    Student loadUserByUsername(String username, String password);
    Collection<Student> listAllUsers();
}
