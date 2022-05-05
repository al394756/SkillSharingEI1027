package com.example.SkillSharingEI1027.modelo;

public class Collaboration {

    private String idCollaboration;
    private int assessmentScore;
    private int collaborationState;
    private OffeRequest idOffer;
    private OffeRequest idRequest;
    private int hours;

    public Collaboration() {
        collaborationState = 0;
    }

    public Collaboration(OffeRequest idRequest, OffeRequest idOffer) {
        this.idRequest = idRequest;
        this.idOffer = idOffer;
    }

    public void setCollaborationState(int valor) {
        if (valor >= 0 && valor < CollaborationState.values().length){
            collaborationState=valor;
        }
    }
    public void setAssessmentScore(int valor) {
        if (valor >= 0 && valor < AssesmentScore.values().length){
            assessmentScore=valor;
        }
    }

    public String getIdCollaboration() {
        return idCollaboration;
    }

    public void setIdCollaboration(String idCollaboration) {
        this.idCollaboration = idCollaboration;
    }

    public int getAssessmentScore() {
        return assessmentScore;
    }

    public int getCollaborationState() {
        return collaborationState;
    }

    public OffeRequest getIdOffer() {
        return idOffer;
    }

    public void setIdOffer(OffeRequest idOffer) {
        this.idOffer = idOffer;
    }

    public OffeRequest getIdRequest() {
        return idRequest;
    }

    public void setIdRequest(OffeRequest idRequest) {
        this.idRequest = idRequest;
    }



    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    @Override
    public String toString() {
        return "Collaboration{" +
                "idCollaboration='" + idCollaboration + '\'' +
                ", assessmentScore=" + assessmentScore +
                ", collaborationState=" + collaborationState +
                ", idOffer='" + idOffer + '\'' +
                ", idRequest='" + idRequest + '\'' +
                ", hours=" + hours +
                '}';
    }
}