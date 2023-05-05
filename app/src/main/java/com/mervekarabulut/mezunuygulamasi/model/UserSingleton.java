package com.mervekarabulut.mezunuygulamasi.model;

public class UserSingleton {
    private DataHolder selectedDataHolder;
    private static UserSingleton userSingleton;

    private UserSingleton() {

    }

    public DataHolder getSelectedDataHolder() {
        return selectedDataHolder;
    }

    public void setChosenDataHolder(DataHolder dataHolder) {
        this.selectedDataHolder = selectedDataHolder;
    }

    public static UserSingleton getInstance() {
        if (userSingleton == null) {
            userSingleton = new UserSingleton();
        }
        return userSingleton;

    }
}
