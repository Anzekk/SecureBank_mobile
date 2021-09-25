package com.secure_bank.bank.Model;

import com.secure_bank.bank.Util.CustomGsonBuilder;

public class EmptyResult {
    public String Error = "";
    public int ErrorCode = 0;

    public static EmptyResult convertStringJsonToEmptyResult(String str) {
        return CustomGsonBuilder.getBuilder().create().fromJson(str, EmptyResult.class);
    }
}
