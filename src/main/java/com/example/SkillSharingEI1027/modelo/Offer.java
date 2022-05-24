package com.example.SkillSharingEI1027.modelo;

public class Offer extends OffeRequest{
    public Offer(){
        super("Offer","of","offer");
    }
    public Offer(OffeRequest offeRequest) {
        super(offeRequest);
    }

    @Override
    public int compareTo(OffeRequest offeRequest) {
        return this.getStartDate().compareTo(offeRequest.getStartDate());
    }
}
