package com.example.SkillSharingEI1027.modelo;

public enum CollaborationState {
    //Aún no se ha realizado
    PENDIENTE(0),
    CONFIRMADO(1),
    //Ya se ha realizado
    ESPERANDO_PUNTUACION(2),
    FINALIZADO(3),
    //No se realiza
    CANCELADO(4);

    private final int id;

    CollaborationState(int id){
        this.id=id;
    }


    public int getId() {
        return id;
    }
}
