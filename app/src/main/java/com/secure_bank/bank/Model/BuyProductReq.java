package com.secure_bank.bank.Model;

import com.google.gson.annotations.SerializedName;
import com.secure_bank.bank.Util.CustomGsonBuilder;

public class BuyProductReq {
    @SerializedName("id")
    public String Id;
    @SerializedName("quantity")
    public String Quantity;
    @SerializedName("price")
    public String Price;

    public String Error = "";
    public int ErrorCode = 0;

    public static BuyProductReq convertStringJsonToBuyProductReq(String str) {
        return CustomGsonBuilder.getBuilder().create().fromJson(str, BuyProductReq.class);
    }
}
