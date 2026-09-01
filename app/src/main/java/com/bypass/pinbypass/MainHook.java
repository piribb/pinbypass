package com.bypass.pinbypass;

import io.github.libxposed.api.XposedInterface;
import io.github.libxposed.api.XposedModule;
import io.github.libxposed.api.XposedModuleInterface.PackageLoadedParam;

import java.lang.reflect.Method;

public class MainHook extends XposedModule {

    private static final String TARGET_PKG = "com.google.android.gms.supervision";

    public MainHook() {
    }

    @Override
    public void onPackageLoaded(PackageLoadedParam param) {
        if (!param.getPackageName().equals(TARGET_PKG)) return;
        try {
            ClassLoader cl = param.getDefaultClassLoader();
            Class<?> rodClass = cl.loadClass("rod");
            Method m = rodClass.getDeclaredMethod("S", String.class);
            hook(m).intercept(new XposedInterface.Hooker() {
                @Override
                public Object intercept(XposedInterface.Chain chain) {
                    return true;
                }
            });
            log(android.util.Log.INFO, "PinBypass", "hooked rod.S()");
        } catch (Throwable e) {
            log(android.util.Log.ERROR, "PinBypass", "hook failed: " + e);
        }
    }
}