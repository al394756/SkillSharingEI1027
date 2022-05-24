package com.example.SkillSharingEI1027.modelo;

import java.util.Comparator;

public class StudentComparatorById extends StudentComparator implements Comparator<Student> {



    public StudentComparatorById() {
        super();
    }
    public StudentComparatorById(Sorter sorter) {
        super(sorter);
    }

    @Override
    public int compare(Student o1, Student o2) {
        if (getSorter().isFromTopToBot())
            return -(o1.getIdStudent().compareTo(o2.getIdStudent()));
        return o1.getIdStudent().compareTo(o2.getIdStudent());

    }

}
