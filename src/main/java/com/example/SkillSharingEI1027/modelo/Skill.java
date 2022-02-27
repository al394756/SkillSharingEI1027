package com.example.SkillSharingEI1027.modelo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Skill {
    String idSkill;
    String name;
    String description;
    int level;

    public Skill() {
    }

    public String getIdSkill() {
        return idSkill;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getLevel() {
        return level;
    }

    public void setIdSkill(String idSkill) {
        this.idSkill = idSkill;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLevel(int valor) {
        if (valor >= 0 && valor < SkillLevel.values().length){
            level=valor;
        }
    }

    @Override
    public String toString() {
        return "Skill{" +
                "idSkill='" + idSkill + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", level='" + level + '\'' +
                '}';
    }

    public static void main(String[] args) {
        Skill s=new Skill();
        System.out.println(s.toString());
        s.setIdSkill("prueba");
        s.setDescription("esto es un prueba");
        s.setName("pruebaaaa");
        s.setLevel(2);
        System.out.println(s.toString());

    }
}
