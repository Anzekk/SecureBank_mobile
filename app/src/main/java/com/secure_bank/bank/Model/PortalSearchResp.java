package com.secure_bank.bank.Model;

import com.google.gson.annotations.SerializedName;
import com.secure_bank.bank.Util.CustomGsonBuilder;

import java.util.List;

public class PortalSearchResp {
    @SerializedName("searchString")
    public String SearchString;
    @SerializedName("results")
    public List<String> Data;

    public String Error = "";
    public int ErrorCode = 0;

    public static PortalSearchResp convertStringJsonToPortalSearchResp(String str) {
        return CustomGsonBuilder.getBuilder().create().fromJson(str, PortalSearchResp.class);
    }
}
