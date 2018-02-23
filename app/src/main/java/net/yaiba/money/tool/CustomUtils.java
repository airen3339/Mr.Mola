package net.yaiba.money.tool;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import net.yaiba.money.R;

public class CustomUtils {


    public static String getVersion(Context context)//获取版本号
    {
        try {
            PackageInfo pi=context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return context.getString(R.string.version_unknown);
        }
    }

    public static int getVersionCode(Context context)//获取版本号(内部识别号)
    {
        try {
            PackageInfo pi=context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }



}
