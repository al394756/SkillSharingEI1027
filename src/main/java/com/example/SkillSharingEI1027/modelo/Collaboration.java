package com.example.SkillSharingEI1027.modelo;

public class Collaboration {

    private String idCollaboration;
    private int assessmentScore;
    private int collaborationState;
    private String idOffer;
    private String idRequest;
    private String idStudent;
    private int hours;

    public Collaboration() {
        collaborationState = 0;
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

    public String getIdOffer() {
        return idOffer;
    }

    public void setIdOffer(String idOffer) {
        this.idOffer = idOffer;
    }

    public String getIdRequest() {
        return idRequest;
    }

    public void setIdRequest(String idRequest) {
        this.idRequest = idRequest;
    }

    public String getIdStudent() {
        return idStudent;
    }

    public void setIdStudent(String idStudent) {
        this.idStudent = idStudent;
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
                ", idStudent='" + idStudent + '\'' +
                ", hours=" + hours +
                '}';
    }
}