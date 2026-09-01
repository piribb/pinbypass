package com.bypass.pinbypass;

import io.github.libxposed.api.XposedInterface;
import io.github.libxposed.api.XposedModule;
import io.github.libxposed.api.XposedModuleInterface;
import io.github.libxposed.api.annotations.BeforeInvocation;
import io.github.libxposed.api.annotations.XposedHooker;

public class MainHook extends XposedModule {

    private static final String TAG = "PinBypass";
    private static final String TARGET_PKG = "com.google.android.gms.supervision";

    public MainHook(XposedInterface base, XposedModuleInterface.ModuleLoadedParam param) {
        super(base, param);
        log(TAG + " loaded");
    }

    @Override
    public void onPackageLoaded(XposedModuleInterface.PackageLoadedParam param) {
        if (!param.getPackageName().equals(TARGET_PKG)) return;
        try {
            Class<?> rodClass = param.getClassLoader().loadClass("rod");
            hookMethod(
                rodClass.getDeclaredMethod("S", String.class),
                RodSHooker.class
            );
        } catch (Exception e) {
            log(TAG + ": hook failed — " + e.getMessage());
        }
    }

    @XposedHooker
    static class RodSHooker implements XposedInterface.Hooker {
        @BeforeInvocation
        public static void before(XposedInterface.BeforeHookCallback callback) {
            callback.returnAndSkip(true);
        }
    }
}