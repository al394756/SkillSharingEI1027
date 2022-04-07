package com.example.SkillSharingEI1027.dao;

import com.example.SkillSharingEI1027.modelo.Student;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.sql.DataSource;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


//Contiene métodos para acceder a usuarios YA registrados
@Repository
public class StudentDao {
    private JdbcTemplate jdbcTemplate;


    //Obtiene el jdbc a partir de Data Source
    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //Añade el Student a la BBDD
    public void registerStudent(Student student){
        BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
        String passw = passwordEncryptor.encryptPassword(student.getPassword());
        String nombre = formatoNombre(student.getName());
        String apellido = formatoNombre(student.getSurname());
        String nombreCompleto = nombre + " " + apellido;
        String id = idGenerator();
        jdbcTemplate.update("INSERT INTO Student VALUES(?,?,?,?,?,?,?,?,0,False,True)",id,student.getDni().toUpperCase(Locale.ROOT),nombreCompleto,student.getEmail(),
        student.getPhoneNumber(),passw,student.getDegree(),student.getCourse());
    }


    //Genera un id de formato "id000000", id + 6 cifras (permite tener hasta 1000000 usuarios registrados)
    private String idGenerator(){
        AtomicInteger contadorStudents = new AtomicInteger(getCantidadStudents());
        StringBuilder id= new StringBuilder("id");
        int numeroId = contadorStudents.get();
        int numeroCifras = Integer.toString(numeroId).length();
        id.append("0".repeat(Math.max(0, 6 - numeroCifras)));

        return id.toString() +numeroId;
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
        jdbcTemplate.update("UPDATE Student SET activeAccount=false WHERE idStudent=?",student.getIdStudent());
    }

    //Obtenemos Student con su id. Devuelve null si no existe o si la cuenta está inactiva
    public Student getStudent(String idStudent){
        try{
            return jdbcTemplate.queryForObject("SELECT * FROM Student WHERE idStudent = ? AND activeAccount=true", new StudentRowMapper(), idStudent);
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


    //Obtenemos todos los students con cuentas activas o null si no hay ninguno

    public List<Student> getStudentsActivos(){
        try{
            return jdbcTemplate.query("SELECT * FROM Student WHERE activeAccount=true", new StudentRowMapper());
        } catch (EmptyResultDataAccessException e){
            return new ArrayList<>();
        }
    }

    private Integer getCantidadStudents(){
        return jdbcTemplate.queryForObject("SELECT COUNT(name) FROM Student",new IntegerRowMapper());

    }
    public Student loadUserById(String id, String password) {
        Student user = getStudent(id.trim());
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
