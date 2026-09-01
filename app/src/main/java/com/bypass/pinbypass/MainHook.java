package com.bypass.pinbypass;

import android.util.Log;
import io.github.libxposed.api.XposedInterface;
import io.github.libxposed.api.XposedModule;
import io.github.libxposed.api.XposedModuleInterface;
import io.github.libxposed.api.XposedInterface.MethodUnhooker;

import java.lang.reflect.Method;

public class MainHook extends XposedModule {

    private static final String TARGET_PKG = "com.google.android.gms.supervision";

    public MainHook(XposedInterface base, XposedModuleInterface.ModuleLoadedParam param) {
        super(base, param);
    }

    @Override
    public void onPackageLoaded(XposedModuleInterface.PackageLoadedParam param) {
        if (!param.getPackageName().equals(TARGET_PKG)) return;
        try {
            Class<?> rodClass = param.getClassLoader().loadClass("rod");
            Method m = rodClass.getDeclaredMethod("S", String.class);
            hook(m, PinHooker.class);
            log("PinBypass: hooked rod.S()");
        } catch (Throwable e) {
            log("PinBypass: hook failed - " + e);
        }
    }

    public static class PinHooker implements XposedInterface.Hooker {
        public static void before(XposedInterface.BeforeHookCallback callback) {
            callback.returnAndSkip(true);
        }
    }
}