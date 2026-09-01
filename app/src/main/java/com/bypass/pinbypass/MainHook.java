package com.bypass.pinbypass;

import android.util.Log;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MainHook implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        if (!lpparam.packageName.equals("com.google.android.gms.supervision")) return;
        try {
            Class<?> rod = lpparam.classLoader.loadClass("rod");
            XposedHelpers.findAndHookMethod(rod, "S", String.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) {
                    param.setResult(true);
                }
            });
            Log.d("PinBypass", "Hook success");
        } catch (Exception e) {
            Log.e("PinBypass", "Hook failed: " + e.getMessage());
        }
    }
}