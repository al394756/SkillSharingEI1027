package com.example.SkillSharingEI1027.modelo;




public class StudentComparator {
    private Sorter sorter;

    public StudentComparator(Sorter sorter) {
        this.sorter = sorter;
    }

    public Sorter getSorter() {
        return sorter;
    }

    public void setSorter(Sorter sorter) {
        this.sorter = sorter;
    }

    public StudentComparator() {
        this.sorter=new Sorter();
    }
}
