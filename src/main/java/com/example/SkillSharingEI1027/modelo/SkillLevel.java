package com.example.SkillSharingEI1027.modelo;

public enum SkillLevel {
    NOVATO(0),
    MEDIO(1),
    EXPERTO(2);

    private final int id;

    SkillLevel(int id){
        this.id=id;
    }


    public int getId() {
        return id;
    }

}
