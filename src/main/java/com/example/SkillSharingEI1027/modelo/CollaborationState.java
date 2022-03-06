package com.example.SkillSharingEI1027.modelo;

public enum CollaborationState {
    DISPONIBLE(0),
    RESERVADO(1),
    FINALIZADO(2);

    private final int id;

    CollaborationState(int id){
        this.id=id;
    }


    public int getId() {
        return id;
    }
}
