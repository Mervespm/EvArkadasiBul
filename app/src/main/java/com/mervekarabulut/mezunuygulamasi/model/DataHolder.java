package com.mervekarabulut.mezunuygulamasi.model;

import java.io.Serializable;

public class DataHolder implements Serializable {

    String email;
    String password;
    String name;
    String surname;
    String phone;
    String department;
    String year;
    String state;
    String distance;
    String duration;
    String pImage;
    String uid;

    public DataHolder() {
    }
    public DataHolder(String email, String name, String surname, String Uid) {
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.uid = uid;
    }

    public DataHolder(String email, String password, String name, String surname, String phone, String department, String year, String state, String distance, String duration, String pImage) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.department = department;
        this.year = year;
        this.state = state;
        this.distance = distance;
        this.duration = duration;
        this.pImage = pImage;
    }
    public DataHolder(String email, String password, String name, String surname, String enteringYear, String graduateYear) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
    }
    public String getUid() {
        return uid;
    }




    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
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
