package com.example.SkillSharingEI1027.modelo;

public class Request extends OffeRequest{
    public Request(){
        super("Request","rq","request");
    }
    public Request(OffeRequest offeRequest) {
        super(offeRequest);
    }

    @Override
    public int compareTo(OffeRequest offeRequest) {
        return this.getStartDate().compareTo(offeRequest.getStartDate());
    }
}
