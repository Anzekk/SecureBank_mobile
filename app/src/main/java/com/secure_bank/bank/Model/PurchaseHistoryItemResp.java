package com.secure_bank.bank.Model;

import com.google.gson.annotations.SerializedName;
import com.secure_bank.bank.Util.CustomGsonBuilder;

public class PurchaseHistoryItemResp {
    @SerializedName("purchaseTime")
    public String PurchaseTime;
    @SerializedName("name")
    public String Name;
    @SerializedName("description")
    public String Description;
    @SerializedName("quantity")
    public int Quantity;
    @SerializedName("price")
    public double Price;
    @SerializedName("userName")
    public String UserName;

    public String Error = "";
    public int ErrorCode = 0;

    public static PurchaseHistoryItemResp convertStringJsonToPurchaseHistoryItemResp(String str) {
        return CustomGsonBuilder.getBuilder().create().fromJson(str, PurchaseHistoryItemResp.class);
    }
}
