package com.scy.component.hookinstrumentation;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import java.lang.reflect.Method;

public class MyInstrumentation extends Instrumentation {
    private Instrumentation instrumentation;
    public MyInstrumentation(Instrumentation instrumentation) {
        this.instrumentation = instrumentation;
    }

    public ActivityResult execStartActivity(
            Context who, IBinder contextThread, IBinder token, Activity target,
            Intent intent, int requestCode, Bundle options) {
        Intent intent1 = new Intent();
        intent1.setComponent(new ComponentName("com.scy.component.hookinstrumentation", Main2Activity.class.getName()));
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            Method execStartActivity = Instrumentation.class.getDeclaredMethod("execStartActivity", Context.class, IBinder.class
                    , IBinder.class, Activity.class, Intent.class, int.class, Bundle.class);
            return (ActivityResult) execStartActivity.invoke(instrumentation, who, contextThread, token, target, intent1, requestCode, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public Activity newActivity(ClassLoader cl, String className, Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        Log.e("--==","-------------------" + className);
        Log.e("--==","-------------------" + intent);
//        intent.setComponent(new ComponentName("com.scy.component.hookinstrumentation", WithoutRegisterActivity.class.getName()));
        return super.newActivity(cl, "com.scy.component.hookinstrumentation.WithoutRegisterActivity", intent);
    }
}
