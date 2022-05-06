package com.example.SkillSharingEI1027.modelo;

public class Offer extends OffeRequest{
    public Offer(){
        setType("Offer");
        setStart("of");
        setUrl("offer");
    }
    public Offer(OffeRequest of) {
        super.setId(of.getId());
        super.setSkill(of.getSkill());
        super.setStudent(of.getStudent());
        super.setStart(of.getStart());
        super.setDescription(of.getDescription());
        super.setStartDate(of.getStartDate());
        super.setEndDate(of.getEndDate());
        super.setType(of.getType());
        super.setUrl(of.getUrl());
    }
}
