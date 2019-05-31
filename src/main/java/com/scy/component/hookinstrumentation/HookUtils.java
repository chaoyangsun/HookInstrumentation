package com.scy.component.hookinstrumentation;

import android.app.Instrumentation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class HookUtils {

    public static void hookIActivityManager() {

        try {
            //获取mInstrumentation
            Class mThread = Class.forName("android.app.ActivityThread");
            Field mInstrumentation = mThread.getDeclaredField("mInstrumentation");
            mInstrumentation.setAccessible(true);

            //获取 ActivityThread 对象
            Method method = mThread.getDeclaredMethod("currentActivityThread");
            method.setAccessible(true);
            Object o = method.invoke(mThread);

            Instrumentation instrumentation = (Instrumentation) mInstrumentation.get(o);

            //设置
            MyInstrumentation myInstrumentation = new MyInstrumentation(instrumentation);
            mInstrumentation.set(o, myInstrumentation);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
