package com.secure_bank.bank.Listener;

public interface ActionIntegerCompleteListener {
    void actionCompleted(Integer number);

    void actionFailed(String error);
}
