package com.example.SkillSharingEI1027.modelo;

public class Request extends OffeRequest{
    public Request(){
        setType("Request");
        setStart("rq");
        setUrl("request");
    }
    public Request(OffeRequest of) {
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
