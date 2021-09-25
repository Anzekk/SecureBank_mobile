package com.secure_bank.bank.Model;

public class SudoItemModel {
    public boolean isUsed;
    public String name;

    public SudoItemModel(String packageName, boolean status) {
        this.name = packageName;
        this.isUsed = status;
    }
}
