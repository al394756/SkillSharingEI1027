package com.example.SkillSharingEI1027.dao;

import com.example.SkillSharingEI1027.modelo.UserDetails;

import java.util.Collection;

public interface UserDao {
    UserDetails loadUserByUsername(String username, String password);
    Collection<UserDetails> listAllUsers();
}
