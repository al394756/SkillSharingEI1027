package com.example.SkillSharingEI1027.dao;

import com.example.SkillSharingEI1027.modelo.Student;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


import javax.sql.DataSource;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


//Contiene métodos para acceder a usuarios YA registrados
@Repository
public class StudentDao {
    private JdbcTemplate jdbcTemplate;


    public StudentDao(){
        jdbcTemplate=new JdbcTemplate();
    }
    //Obtiene el jdbc a partir de Data Source
    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //Añade el Student a la BBDD y devuelve la id que se genera para ese estudiante
    public String registerStudent(Student student){
        BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
        String passw = passwordEncryptor.encryptPassword(student.getPassword());
        String nombre = formatoNombre(student.getName().trim());
        String apellido = formatoNombre(student.getSurname().trim());
        String nombreCompleto = nombre + " " + apellido;
        String id = idGenerator();
        jdbcTemplate.update("INSERT INTO Student VALUES(?,?,?,?,?,?,?,?,0,False,True,Null)",id,student.getDni().toUpperCase(Locale.ROOT).trim(),nombreCompleto.trim(),student.getEmail().toLowerCase(Locale.ROOT).trim(),
        student.getPhoneNumber(),passw,student.getDegree(),student.getCourse());
        return id;
    }


    //Genera un id de formato "id000000", id + 6 cifras (permite tener hasta 1000000 usuarios registrados)
    private String idGenerator(){
        AtomicInteger contadorStudents = new AtomicInteger(getCantidadStudents());
        int maximo0 = 6;
        int numeroId = contadorStudents.get();
        int numeroCifras = Integer.toString(numeroId).length();
        return "id"+"0".repeat(Math.max(0, maximo0 - numeroCifras)) +numeroId;
    }

    private String formatoNombre(String nombre){
        nombre = nombre.toLowerCase(Locale.ROOT);
        StringBuilder nombreFormato= new StringBuilder();
        String[] arrayNombres = nombre.split(" ");
        boolean primerNombre = false;
        for (String x: arrayNombres){
            if (primerNombre){
                nombreFormato.append(" ");
            }
            String inicial = x.substring(0, 1).toUpperCase(Locale.ROOT);
            String resto = x.substring(1);
            nombreFormato.append(inicial).append(resto);
            primerNombre=true;
        }
        return nombreFormato.toString();
    }
    //No se puede borrar estudiante, solo podemos cancelar su cuenta poniendo activeAccount a false
    public void cancelStudent(Student student){
        jdbcTemplate.update("UPDATE Student SET activeAccount=false, banReason=? WHERE idStudent=?", student.getBanReason(),student.getIdStudent());
    }

    public void unbanStudent(Student student){
        jdbcTemplate.update("UPDATE Student SET activeAccount=true, banReason=? WHERE idStudent=?", student.getBanReason(),student.getIdStudent());
    }

    //Obtenemos Student con su id. Devuelve null si no existe o si la cuenta está inactiva
    public Student getStudentUsingId(String idStudent){
        try{
            return jdbcTemplate.queryForObject("SELECT * FROM Student WHERE idStudent = ?", new StudentRowMapper(), idStudent);
        } catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public Student getStudentUsingEmail(String email){
        try{
            return jdbcTemplate.queryForObject("SELECT * FROM Student WHERE email = ?", new StudentRowMapper(), email);
        } catch (EmptyResultDataAccessException e){
            return null;
        }
    }
    //Comprobamos si existe alguna entrada en la BBDD que contenga algún dato único (Evitamos errores)
    public boolean checkExistingStudentDNI(Student student){
        try {
            jdbcTemplate.queryForObject("SELECT * FROM Student WHERE dni = ?", new StudentRowMapper(), student.getDni());
            return true;
        } catch (EmptyResultDataAccessException e){
            return false;
        }
    }

    public boolean checkExistingStudentEmail(Student student){
        try {
            jdbcTemplate.queryForObject("SELECT * FROM Student WHERE email = ?", new StudentRowMapper(), student.getEmail());
            return true;
        } catch (EmptyResultDataAccessException e){
            return false;
        }
    }


    //Obtenemos todos los students con cuentas activas o lista vacia si no hay ninguno

    public List<Student> getStudentsActivos(){
        try{
            return jdbcTemplate.query("SELECT * FROM Student WHERE activeAccount=true", new StudentRowMapper());
        } catch (EmptyResultDataAccessException e){
            return new ArrayList<>();
        }
    }

    public List<Student> getStudents(){
        try{
            return jdbcTemplate.query("SELECT * FROM Student", new StudentRowMapper());
        } catch (EmptyResultDataAccessException e){
            return new ArrayList<>();
        }
    }

    private int getCantidadStudents(){
        return jdbcTemplate.queryForObject("SELECT COUNT(name) FROM Student",Integer.class);

    }
    public Student loadUser(String userInput, String password) {
        Student user;
        if (userInput.contains("@")){
            user = getStudentUsingEmail(userInput.trim().toLowerCase(Locale.ROOT));
        } else{
            user = getStudentUsingId(userInput.trim().toLowerCase(Locale.ROOT));
        }
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

}
