package com.secure_bank.bank.Util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class AppChecker {

    public static String[] applicationPackages = {
            "com.noshufou.android.su",
            "com.thirdparty.superuser",
            "eu.chainfire.supersu",
            "com.koushikdutta.superuser"
    };

    public static String[] applicationPaths = {
            "/sbin/su",
            "/su/bin/su",
            "/data/local/su",
            "/data/local/bin/su",
            "/data/local/xbin/su",
            "/system/su",
            "/system/bin/su",
            "/system/xbin/su",
            "/system/sd/xbin/su",
            "/system/bin/.ext/.su",
            "/system/bin/failsafe/su",
            "/system/app/Superuser.apk",
    };


    public static boolean IsRoot(Context context) {
        return CheckTestKeys() || CheckDevKeys() || CheckRootApplications(context) || CheckRootProcess();
    }

    public static boolean InstalledOnDevice(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        boolean isInstalled;
        try {
            packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            isInstalled = true;
        } catch (Exception ex) {
            Log.e(Global.TAG, "App not installed");
            isInstalled = false;
        }
        return isInstalled;
    }

    public static boolean FindPathOnDevice(String path) {
        return new File(path).exists();
    }

    public static boolean CheckTestKeys() {
        try {
            String key_tags = android.os.Build.TAGS;
            return key_tags != null && key_tags.contains("test-keys");
        } catch (Exception ex) {
            Log.e(Global.TAG, ex.toString());
            return false;
        }
    }

    public static boolean CheckDevKeys() {
        try {
            String key_tags = android.os.Build.TAGS;
            return key_tags != null && key_tags.contains("dev-keys");
        } catch (Exception ex) {
            Log.e(Global.TAG, ex.toString());
            return false;
        }
    }


    public static boolean CheckRootApplications(Context context) {
        boolean foundRootElement = false;

        try {
            for (String path : applicationPaths) {
                FindPathOnDevice(path);
            }
        } catch (Exception ex) {
            Log.e(Global.TAG, ex.toString());
        }
        try {
            for (String pack : applicationPackages) {
                if (InstalledOnDevice(context, pack)) {
                    foundRootElement = true;
                    break;
                }
            }
        } catch (Exception ex) {
            Log.e(Global.TAG, ex.toString());
        }
        return foundRootElement;
    }

    public static boolean CheckRootProcess() {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(new String[]{"/system/xbin/which", "su"});
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            return in.readLine() != null;
        } catch (Throwable t) {
            return false;
        } finally {
            if (process != null) process.destroy();
        }
    }
}
