package com.example.SkillSharingEI1027.modelo;

public enum AssesmentScore {
    UNAESTRELLA(1),
    DOSESTRELLAS(2),
    TRESESTRELLAS(3),
    CUATROESTRELLAS(4),
    CINCOESTRELLAS(5);

    private final int id;

    AssesmentScore(int id){
        this.id=id;
    }


    public int getId() {
        return id;
    }
}
