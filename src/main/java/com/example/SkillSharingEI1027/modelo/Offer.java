package com.example.SkillSharingEI1027.modelo;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

public class Offer {
    private String idOffer;
    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE)
    private LocalDate startDate;
    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE)
    private LocalDate endDate;
    private String description;
    private String idSkill;
    private String idStudent;

    public void setIdOffer(String idOffer) {
        this.idOffer = idOffer;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
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

    @Override
    public String toString() {
        return "Offer{" +
                "idOffer='" + idOffer + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", description='" + description + '\'' +
                ", idSkill='" + idSkill + '\'' +
                ", idStudent='" + idStudent + '\'' +
                '}';
    }
}
