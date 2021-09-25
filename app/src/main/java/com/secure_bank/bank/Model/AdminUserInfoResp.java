package com.secure_bank.bank.Model;

import com.google.gson.annotations.SerializedName;
import com.secure_bank.bank.Util.CustomGsonBuilder;

public class AdminUserInfoResp {
    @SerializedName("name")
    public String Name;
    @SerializedName("surname")
    public String SurName;
    @SerializedName("username")
    public String Username;
    @SerializedName("role")
    public int Role;

    public String Error = "";
    public int ErrorCode = 0;

    public static AdminUserInfoResp convertStringJsonToAdminUserInfoResp(String str) {
        return CustomGsonBuilder.getBuilder().create().fromJson(str, AdminUserInfoResp.class);
    }
}
