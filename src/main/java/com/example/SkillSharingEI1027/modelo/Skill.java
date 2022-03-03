package com.example.SkillSharingEI1027.modelo;

public class Skill {
    private String idSkill;
    private String name;
    private String description;
    private int level;

    public Skill() {}

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

}
