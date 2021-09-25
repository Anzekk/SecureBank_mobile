package com.secure_bank.bank.Model;

import com.google.gson.annotations.SerializedName;
import com.secure_bank.bank.Util.CustomGsonBuilder;

public class StoreItem {
    @SerializedName("id")
    public int Id;
    @SerializedName("name")
    public String Name;
    @SerializedName("description")
    public String Description;
    @SerializedName("price")
    public double Price;
    @SerializedName("installments")
    public int Installments;

    public int ErrorCode = 0;
    public String Error = "";

    public static StoreItem convertStringJsonToStoreItem(String str) {
        return CustomGsonBuilder.getBuilder().create().fromJson(str, StoreItem.class);
    }
}
