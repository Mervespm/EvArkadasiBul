package com.mervekarabulut.mezunuygulamasi.model;

public class Post {
    String email;
    String comment;
    String url;

    public Post(String email, String comment, String url) {
        this.email = email;
        this.comment = comment;
        this.url = url;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
