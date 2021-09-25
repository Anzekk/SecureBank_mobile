package com.secure_bank.bank.Listener;

import java.util.List;

public interface ActionHTMLCompleteListener {
    void actionCompleted(List<String> data);

    void actionFailed(String error);
}
