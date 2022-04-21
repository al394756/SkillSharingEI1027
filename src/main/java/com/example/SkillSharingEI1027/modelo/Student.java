package com.example.SkillSharingEI1027.modelo;

public class Student implements Comparable<Student>{
    private String idStudent;
    private String dni;
    private String name;
    private String surname;
    private String email;
    private int phoneNumber;
    private String password;
    private String degree;
    private int course;
    private int balanceHours;
    private boolean skpMember;
    private boolean activeAccount;
    private String banReason;

    public Student(){
        balanceHours=0;
        skpMember=false;
        activeAccount=true;
        banReason=null;
    }

    public String getSurname() { return surname; }

    public String getIdStudent() {
        return idStudent;
    }

    public String getDni() {
        return dni;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public String getDegree() {
        return degree;
    }

    public int getCourse() {
        return course;
    }

    public int getBalanceHours() {
        return balanceHours;
    }

    public boolean isSkpMember() {
        return skpMember;
    }

    public boolean isActiveAccount() {
        return activeAccount;
    }

    public String getPassword() {
        return password;
    }

    public void setIdStudent(String idStudent) {
        this.idStudent = idStudent;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public void setCourse(int course) {
        this.course = course;
    }

    public void setBalanceHours(int balanceHours) {
        this.balanceHours = balanceHours;
    }

    public void setSkpMember(boolean skpMember) {
        this.skpMember = skpMember;
    }

    public void setActiveAccount(boolean activeAccount) {
        this.activeAccount = activeAccount;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSurname(String surname){ this.surname=surname;}

    public void setBanReason(String banReason) {
        this.banReason = banReason;
    }

    public String getBanReason() {
        return banReason;
    }

    @Override
    public String toString() {
        return "Student{" +
                "idStudent='" + idStudent + '\'' +
                ", dni='" + dni + '\'' +
                ", name='" + name + surname+ '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber=" + phoneNumber +
                ", degree='" + degree + '\'' +
                ", course=" + course +
                ", balanceHours=" + balanceHours +
                ", skpMember=" + skpMember +
                ", activeAccount=" + activeAccount +
                ", banReason="+banReason+
                '}';
    }


    public int compareTo(Student o, Sorter sorter) {
        if (sorter.getType().equals("idStudent")) {
            if (sorter.isFromTopToBot())
                return -(this.getIdStudent().compareTo(o.getIdStudent()));
            return this.getIdStudent().compareTo(o.getIdStudent());
        } else if (sorter.getType().equals("balanceHours")){

                if (sorter.isFromTopToBot()){
                    if (o.getBalanceHours() >= this.getBalanceHours())
                        return -1;
                    else
                        return 1;
                } else{
                    if (o.getBalanceHours() >= this.getBalanceHours())
                        return 1;
                    else
                        return -1;
                }

        }
        return 0;

    }

    @Override
    public int compareTo(Student o) {
        return this.getIdStudent().compareTo(o.getIdStudent());
    }
}
