package com.secure_bank.bank.Listener;

public interface ActionCompleteListener {
    void actionCompleted();

    void actionFailed(String error);
}
