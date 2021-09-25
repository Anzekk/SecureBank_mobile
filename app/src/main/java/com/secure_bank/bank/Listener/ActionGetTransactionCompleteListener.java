package com.secure_bank.bank.Listener;

import com.secure_bank.bank.Model.TransactionResp;

public interface ActionGetTransactionCompleteListener {
    void actionCompleted(TransactionResp transactionRespsList);

    void actionFailed(String error);
}
