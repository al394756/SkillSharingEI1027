package com.example.SkillSharingEI1027.modelo;

import java.util.Date;

public class Offer {
    private String idOffer;
    private Date startDate;
    private Date endDate;
    private String description;
    private String idSkill;
    private String idStudent;

    public void setIdOffer(String idOffer) {
        this.idOffer = idOffer;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIdSkill(String idSkill) {
        this.idSkill = idSkill;
    }

    public void setIdStudent(String idStudent) {
        this.idStudent = idStudent;
    }

    public String getIdOffer() {
        return idOffer;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getDescription() {
        return description;
    }

    public String getIdSkill() {
        return idSkill;
    }

    public String getIdStudent() {
        return idStudent;
    }
}
