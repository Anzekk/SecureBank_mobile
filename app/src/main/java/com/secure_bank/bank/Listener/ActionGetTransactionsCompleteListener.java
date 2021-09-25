package com.secure_bank.bank.Listener;

import com.secure_bank.bank.Model.TransactionResp;

import java.util.List;

public interface ActionGetTransactionsCompleteListener {
    void actionCompleted(List<TransactionResp> transactionRespsList);

    void actionFailed(String error);
}
