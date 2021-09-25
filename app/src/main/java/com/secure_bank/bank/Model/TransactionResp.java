package com.secure_bank.bank.Model;

import com.google.gson.annotations.SerializedName;
import com.secure_bank.bank.Util.CustomGsonBuilder;

public class TransactionResp {
    @SerializedName("id")
    public int id;
    @SerializedName("senderId")
    public String senderId;
    @SerializedName("receiverId")
    public String receiverId;
    @SerializedName("dateTime")
    public String dateTime;
    @SerializedName("reason")
    public String reason;
    @SerializedName("amount")
    public double amount;
    @SerializedName("reference")
    public String reference;
    @SerializedName("senderName")
    public String senderName;
    @SerializedName("senderSurname")
    public String senderSurname;
    @SerializedName("receiverName")
    public String receiverName;
    @SerializedName("receiverSurname")
    public String receiverSurname;


    public int ErrorCode = 0;
    public String Error = "";

    public static TransactionResp convertStringJsonToResponseResp(String str) {
        return CustomGsonBuilder.getBuilder().create().fromJson(str, TransactionResp.class);
    }
}
