package com.secure_bank.bank.Model;

import com.google.gson.annotations.SerializedName;
import com.secure_bank.bank.Util.CustomGsonBuilder;

public class AccountBalanceResp {
    @SerializedName("balance")
    public int Balance;

    public String Error = "";
    public int ErrorCode = 0;

    public static AccountBalanceResp convertStringJsonToAccountBalanceResp(String str) {
        return CustomGsonBuilder.getBuilder().create().fromJson(str, AccountBalanceResp.class);
    }
}
