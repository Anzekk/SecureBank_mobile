package com.secure_bank.bank.Listener;

import com.secure_bank.bank.Model.UserModel;

public interface ActionLoginCompleteListener {
    void actionCompleted(UserModel user);

    void actionFailed(String error);
}
