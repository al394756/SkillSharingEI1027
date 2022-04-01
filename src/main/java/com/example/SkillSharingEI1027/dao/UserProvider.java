package com.example.SkillSharingEI1027.dao;

import com.example.SkillSharingEI1027.modelo.Student;
import org.jasypt.util.password.BasicPasswordEncryptor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class UserProvider extends StudentDao implements UserDao{

    final Map<String, Student> knownUsers = new HashMap<>();

    public UserProvider(){
        BasicPasswordEncryptor encryptor = new BasicPasswordEncryptor();


    }

    @Override
    public Student loadUserByUsername(String username, String password) {
        Student user = knownUsers.get(username.trim());
        if (user == null)
            return null; // Usuari no trobat
        // Contrasenya
        BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
        if (passwordEncryptor.checkPassword(password, user.getPassword())) {
            // Es deuria esborrar de manera segura el camp password abans de tornar-lo
            return user;
        }
        else {
            return null; // bad login!
        }
    }

    @Override
    public Collection<Student> listAllUsers() {
        return knownUsers.values();
    }
}
