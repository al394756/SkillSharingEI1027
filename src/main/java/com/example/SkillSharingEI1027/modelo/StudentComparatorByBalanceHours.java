package com.example.SkillSharingEI1027.modelo;

import java.util.Comparator;

public class StudentComparatorByBalanceHours  extends StudentComparator implements Comparator<Student> {

    @Override
    public int compare(Student a, Student b) {
        if (getSorter().isFromTopToBot())
            return -(Float.compare(a.getBalanceHours(), b.getBalanceHours()));
        return Float.compare(a.getBalanceHours(), b.getBalanceHours());
    }
    public StudentComparatorByBalanceHours() {
        super();
    }

    public StudentComparatorByBalanceHours(Sorter sorter) {
        super(sorter);
    }
}