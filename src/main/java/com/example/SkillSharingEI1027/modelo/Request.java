package com.example.SkillSharingEI1027.modelo;

import java.util.Date;

public class Request {
    private String idRequest;
    private String idSkill;
    private Date startDate;
    private Date endDate;
    private String description;
    private String idStudent;

    public String getIdRequest() {
        return idRequest;
    }

    public void setIdRequest(String idRequest) {
        this.idRequest = idRequest;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIdStudent() {
        return idStudent;
    }

    public void setIdStudent(String idStudent) {
        this.idStudent = idStudent;
    }

    public String getIdSkill() {
        return idSkill;
    }

    public void setIdSkill(String idSkill) {
        this.idSkill = idSkill;
    }

    @Override
    public String toString() {
        return "Request{" +
                "idRequest='" + idRequest + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", description='" + description + '\'' +
                ", idStudent='" + idStudent + '\'' +
                ", idSkill='" + idSkill + '\'' +
                '}';
    }
}
