package com.example.SkillSharingEI1027.modelo;

public enum CollaborationScore {
    UNAESTRELLA(1),
    DOSESTRELLAS(2),
    TRESESTRELLAS(3),
    CUATROESTRELLAS(4),
    CINCOESTRELLAS(5);

    private final int id;

    CollaborationScore(int id){
        this.id=id;
    }


    public int getId() {
        return id;
    }
}
