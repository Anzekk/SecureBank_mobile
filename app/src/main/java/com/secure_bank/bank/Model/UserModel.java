package com.secure_bank.bank.Model;

import com.google.gson.annotations.SerializedName;
import com.secure_bank.bank.Util.CustomGsonBuilder;

public class UserModel {
    @SerializedName("id")
    public String Id;
    @SerializedName("userName")
    public String UserName;
    @SerializedName("password")
    public String Password;
    @SerializedName("name")
    public String Name;
    @SerializedName("surname")
    public String Surname;
    @SerializedName("userRight")
    public Integer UserRight;
    @SerializedName("cookie")
    public String Cookie;
    @SerializedName("status")
    public String Status;
    @SerializedName("token")
    public String Token;
    public String Error = "";
    public int ErrorCode = 0;
    String photoDir;

    public static UserModel convertStringJsonToUserModel(String str) {
        return CustomGsonBuilder.getBuilder().create().fromJson(str, UserModel.class);
    }
}
