package com.secure_bank.bank.Model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;

import com.secure_bank.bank.Util.CustomGsonBuilder;
import com.secure_bank.bank.Util.Global;

import java.io.File;
import java.io.FileOutputStream;

public class ProfileImageResponseRecord {

    public String Error = "";
    public int ErrorCode = 0;

    public static ProfileImageResponseRecord convertStringJsonToResponseRecord(String str) {
        return CustomGsonBuilder.getBuilder().create().fromJson(str, ProfileImageResponseRecord.class);
    }

    public static void SaveImage(String imageString) {

        byte[] decodedString = Base64.decode(imageString, Base64.NO_WRAP);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        if (Global.person.Name == null || Global.person.Name.equals(""))
            Global.person.Name = Global.person.UserName.replace("@", "").replace(".", "");

        String filename = Global.person.Name + ".png";
        File file = Environment.getExternalStorageDirectory();
        File dest = new File(file, filename);
        Global.person.photoDir = dest.getPath();

        try {
            FileOutputStream out = new FileOutputStream(dest);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
