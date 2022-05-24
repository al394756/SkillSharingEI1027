package com.example.SkillSharingEI1027.modelo;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public abstract class OffeRequest implements Comparable<OffeRequest>{
    private String id;
    private Skill skill;
    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE)
    private LocalDate startDate;
    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE)
    private LocalDate endDate;
    private String description;
    private Student student;
    private String type;
    private String start;
    private String url;

    protected OffeRequest(String type, String start, String url) {
        student=new Student();
        skill=new Skill();
        this.type = type;
        this.start = start;
        this.url = url;
    }
    protected OffeRequest(OffeRequest offeRequest) {
        this.id=offeRequest.getId();
        this.skill=offeRequest.getSkill();
        this.startDate=offeRequest.getStartDate();
        this.endDate=offeRequest.getEndDate();
        this.description=offeRequest.getDescription();
        this.student=offeRequest.getStudent();
        this.type=offeRequest.getType();
        this.start=offeRequest.getStart();
        this.url=offeRequest.getUrl();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }



    @Override
    public String toString() {
        return "OffeRequest{" +
                "id='" + id + '\'' +
                ", skill=" + skill +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", description='" + description + '\'' +
                ", student=" + student +
                ", type='" + type + '\'' +
                ", start='" + start + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    public String getType(){
        return type;
    }
    public String getStart(){
        return start;
    }
    public String getUrl(){
        return url;
    }
    public void setType(String type){
        this.type=type;
    }
    public void setStart(String start){
        this.start=start;
    }
    public void setUrl(String url){
        this.url=url;
    }
}
