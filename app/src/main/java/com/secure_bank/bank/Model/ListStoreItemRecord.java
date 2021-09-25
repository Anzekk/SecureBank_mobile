package com.secure_bank.bank.Model;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ListStoreItemRecord {
    @SerializedName("")
    public List<StoreItem> data = new ArrayList<>();

    public int ErrorCode = 0;
    public String Error = "";

    public static List<StoreItem> convertStringJsonToResponseRecord(String str) {
        List<StoreItem> tmp = new ArrayList<>();
        try {
            JSONArray jr = new JSONArray(str);
            for (int i = 0; i < jr.length(); i++) {
                tmp.add(StoreItem.convertStringJsonToStoreItem(jr.getString(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return tmp;
    }
}
