package com.secure_bank.bank.Model;

import com.google.gson.annotations.SerializedName;
import com.secure_bank.bank.Util.CustomGsonBuilder;

import java.util.List;

public class AdminUserInfoRespDataTableResp {
    @SerializedName("recordsTotal")
    public int RecordsTotal;
    @SerializedName("recordsFiltered")
    public int RecordsFiltered;
    @SerializedName("data")
    public List<AdminUserInfoResp> Data;

    public String Error = "";
    public int ErrorCode = 0;

    public static AdminUserInfoRespDataTableResp convertStringJsonToAdminUserInfoRespDataTableResp(String str) {
        return CustomGsonBuilder.getBuilder().create().fromJson(str, AdminUserInfoRespDataTableResp.class);
    }
}
