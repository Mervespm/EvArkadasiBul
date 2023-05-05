package com.mervekarabulut.mezunuygulamasi.model;
import android.graphics.Bitmap;

public class Singleton {

    private Post selectedPost;
    private static Singleton singleton;

    private Singleton() {

    }

    public Post getSelectedPost() {
        return selectedPost;
    }

    public void setChosenPost(Post selectedPost) {
        this.selectedPost = selectedPost;
    }

    public static Singleton getInstance() {
        if (singleton == null) {
            singleton = new Singleton();
        }
        return singleton;

    }

}