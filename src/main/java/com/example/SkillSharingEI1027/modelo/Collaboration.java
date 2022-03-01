package com.example.SkillSharingEI1027.modelo;

public class Collaboration {

    private String idCollaboration;
    private int assessmentScore;
    private boolean collaborationState;
    private String idOffer;
    private String idRequest;
    private int hours;

    public String getIdCollaboration() {
        return idCollaboration;
    }

    public void setIdCollaboration(String idCollaboration) {
        this.idCollaboration = idCollaboration;
    }

    public int getAssessmentScore() {
        return assessmentScore;
    }

    public void setAssessmentScore(int assessmentScore) {
        this.assessmentScore = assessmentScore;
    }

    public boolean isCollaborationState() {
        return collaborationState;
    }

    public void setCollaborationState(boolean collaborationState) {
        this.collaborationState = collaborationState;
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