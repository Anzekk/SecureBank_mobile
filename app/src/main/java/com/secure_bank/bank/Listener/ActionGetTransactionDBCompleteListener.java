package com.secure_bank.bank.Listener;

import com.secure_bank.bank.Model.TransactionDBModel;

public interface ActionGetTransactionDBCompleteListener {
    void actionCompleted(TransactionDBModel record);

    void actionFailed(String error);
}
