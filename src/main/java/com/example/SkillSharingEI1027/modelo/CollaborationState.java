package com.example.SkillSharingEI1027.modelo;

public enum CollaborationState {
    PENDIENTE(0),
    CONFIRMADO(1),
    CANCELADO(2);

    private final int id;

    CollaborationState(int id){
        this.id=id;
    }


    public int getId() {
        return id;
    }
}
