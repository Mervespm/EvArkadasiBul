package com.mervekarabulut.mezunuygulamasi.model;

public class DataHolder {

    String email;
    String password;
    String name;
    String surname;
    String phone;
    String enteringYear;
    String graduateYear;
    String education;
    String city;
    String pImage;

    public DataHolder() {

    }

    public DataHolder(String email, String password, String name, String surname,String enteringYear, String graduateYear) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.enteringYear = enteringYear;
        this.graduateYear = graduateYear;

    }

    public DataHolder(String name, String surname, String enteringYear, String graduateYear) {
        this.name = name;
        this.surname = surname;
        this.enteringYear = enteringYear;
        this.graduateYear = graduateYear;
    }

    public DataHolder(String email,String name, String surname, String phone, String enteringYear, String graduateYear, String education, String city, String pImage) {
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.enteringYear = enteringYear;
        this.graduateYear = graduateYear;
        this.education = education;
        this.city = city;
        this.pImage = pImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEnteringYear() {
        return enteringYear;
    }

    public void setEnteringYear(String enteringYear) {
        this.enteringYear = enteringYear;
    }

    public String getGraduateYear() {
        return graduateYear;
    }

    public void setGraduateYear(String graduateYear) {
        this.graduateYear = graduateYear;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getpImage() {
        return pImage;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setpImage(String pImage) {
        this.pImage = pImage;
    }
}
