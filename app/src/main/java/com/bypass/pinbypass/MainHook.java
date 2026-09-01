package com.bypass.pinbypass;

import android.util.Log;
import io.github.libxposed.api.XposedInterface;
import io.github.libxposed.api.XposedModule;
import io.github.libxposed.api.XposedModuleInterface.ModuleLoadedParam;
import io.github.libxposed.api.XposedModuleInterface.PackageLoadedParam;
import io.github.libxposed.api.annotations.BeforeInvocation;
import io.github.libxposed.api.annotations.XposedHooker;

import java.lang.reflect.Method;

public class MainHook extends XposedModule {

    private static final String TARGET_PKG = "com.google.android.gms.supervision";

    public MainHook(XposedInterface base, ModuleLoadedParam param) {
        super(base, param);
    }

    @Override
    public void onPackageLoaded(PackageLoadedParam param) {
        if (!param.getPackageName().equals(TARGET_PKG)) return;
        try {
            ClassLoader cl = param.getClassLoader();
            Class<?> rodClass = cl.loadClass("rod");
            Method m = rodClass.getDeclaredMethod("S", String.class);
            hook(m, PinHooker.class);
            log("PinBypass: hooked rod.S()");
        } catch (Throwable e) {
            log("PinBypass: hook failed", e);
        }
    }

    @XposedHooker
    public static class PinHooker implements XposedInterface.Hooker {
        @BeforeInvocation
        public static void before(XposedInterface.BeforeHookCallback callback) {
            callback.returnAndSkip(true);
        }
    }
}