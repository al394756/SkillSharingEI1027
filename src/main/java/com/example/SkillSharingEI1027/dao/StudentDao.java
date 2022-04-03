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
    private AtomicInteger contadorStudents;


    //Obtiene el jdbc a partir de Data Source
    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //Añade el Student a la BBDD
    public void registerStudent(Student student){
        BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
        String passw = passwordEncryptor.encryptPassword(student.getPassword());
        System.out.println(passw);
        String id = idGenerator(student.getName());
        jdbcTemplate.update("INSERT INTO Student VALUES(?,?,?,?,?,?,?,?,0,False,True)",id,student.getDni(),student.getName(),student.getEmail(),
        student.getPhoneNumber(),passw,student.getDegree(),student.getCourse());
    }


    //Genera un id a partir del nombre y el contador de usuarios registrados por ejemplo Pepe Fernández si se registra el número 20 se almacena como pefer20 (2 letras nombre + 3 apellido)
    //DUDA: ¿hacemos que los usuarios sigan el esquema de 5 letras + contador? problema con gente que se registre con solo el nombre
    // por ejemplo si alguien se registra como Pepe que hacemos? opcion: seguimos igual y rellenamos con # u otro simbolo (Pe###30)
    private String idGenerator(String name){
        String[] nombre = name.split(" ");
        contadorStudents = new AtomicInteger(getStudentsActivos().size());
        String id = nombre[0].substring(0,2)+nombre[1].substring(0,3)+contadorStudents.get();
        return id.toLowerCase(Locale.ROOT);
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
