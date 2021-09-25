package com.secure_bank.bank.Listener;

import com.secure_bank.bank.Model.StoreItem;

import java.util.List;

public interface ActionGetStoreItemsCompleteListener {
    void actionCompleted(List<StoreItem> storeItemList);

    void actionFailed(String error);
}
