package com.example.SkillSharingEI1027.dao;

import com.example.SkillSharingEI1027.modelo.Student;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;

//Contiene métodos para acceder a usuarios YA registrados
@Repository
public class StudentDao implements UserDao{
    private JdbcTemplate jdbcTemplate;
    final Map<String, Student> knownUsers = new HashMap<>();

    public StudentDao(){

        BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
        //ACABAR
    }
    //Obtiene el jdbc a partir de Data Source
    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //Añade el Student a la BBDD
    public void registerStudent(Student student){
        jdbcTemplate.update("INSERT INTO Student VALUES(?,?,?,?,?,?,?,?,?,?,?",student.getIdStudent(),student.getDni(),student.getName(),student.getEmail(),
        student.getPhoneNumber(),student.getPassword(),student.getDegree(),student.getCourse(),student.getBalanceHours(),student.isSkpMember(),student.isActiveAccount());
    }

    //No se puede borrar estudiante, solo podemos cancelar su cuenta poniendo activeAccount a false
    public void cancelStudent(Student student){
        jdbcTemplate.update("UPDATE Student SET activeAccount=false WHERE idStudent=?",student.getIdStudent());
    }

    //Obtenemos Student con su id. Devuelve null si no existe o si la cuenta está inactiva
    public Student getStudent(String idStudent){
        try{
            return jdbcTemplate.queryForObject("SELECT * FROM Student WHERE idStudent = ?, activeAccount=true", new StudentRowMapper(), idStudent);
        } catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    //Obtenemos todos los students con cuentas activas o null si no hay ninguno

    public List<Student> getStudentsActivos(){
        try{
            return jdbcTemplate.query("SELECT * FROM Student WHERE activeAccount=true", new StudentRowMapper());
        } catch (EmptyResultDataAccessException e){
            return new ArrayList<>();
        }
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
