# HookInstrumentation
Hook ActivityThread里的mInstrumentation来启动一个未注册的Activity

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
  
  //自定义MyInstrumentation
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
        return super.newActivity(cl, "com.scy.component.hookinstrumentation.WithoutRegisterActivity", intent);
    }
}
