package com.secure_bank.bank.Model;

import com.google.gson.annotations.SerializedName;
import com.secure_bank.bank.Util.CustomGsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

public class TransactionDBModel {
    @SerializedName("id")
    public int Id = 0;
    @SerializedName("senderId")
    public String SenderId;
    @SerializedName("receiverId")
    public String ReceiverId;
    @SerializedName("transactionDateTime")
    public String TransactionDateTime;
    @SerializedName("reason")
    public String Reason;
    @SerializedName("amount")
    public int Amount;
    @SerializedName("reference")
    public String Reference;

    public String Error = "";
    public int ErrorCode = 0;

    public TransactionDBModel() {

    }

    public TransactionDBModel(String senderId, String receiverId, String transactionDateTime
            , String reason, String reference, int amount) {
        this.SenderId = senderId;
        this.ReceiverId = receiverId;
        this.TransactionDateTime = transactionDateTime;
        this.Reason = reason;
        this.Reference = reference;
        this.Amount = amount;
    }

    public static TransactionDBModel convertStringJsonToTransactionDBModel(String str) {
        return CustomGsonBuilder.getBuilder().create().fromJson(str, TransactionDBModel.class);
    }

    public static String getJsonBody(TransactionDBModel data) throws JSONException {
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("id", data.Id);
        jsonBody.put("senderId", data.SenderId);
        jsonBody.put("receiverId", data.ReceiverId);
        jsonBody.put("transactionDateTime", data.TransactionDateTime);
        jsonBody.put("reason", data.Reason);
        jsonBody.put("amount", data.Amount);
        jsonBody.put("reference", data.Reference);

        return jsonBody.toString();
    }
}
