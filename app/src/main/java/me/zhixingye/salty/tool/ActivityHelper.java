package me.zhixingye.salty.tool;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.util.EmptyStackException;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class ActivityHelper {
    private static int sActivityForegroundCount;

    private static Stack<Class<? extends Activity>> sActivityClassStack;
    private static Stack<Activity> sActivityInstanceStack;
    private static List<OnAppFrontAndBackListener> sAppFrontAndBackListenerList;

    public synchronized static void init(Application application) {
        sActivityClassStack = new Stack<>();
        sActivityInstanceStack = new Stack<>();
        sAppFrontAndBackListenerList = new LinkedList<>();

        //监听所有activity的生命周期
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, Bundle savedInstanceState) {
                sActivityClassStack.push(activity.getClass());
                sActivityInstanceStack.push(activity);
            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {
                sActivityForegroundCount++;
                if (sActivityForegroundCount == 1) {
                    for (OnAppFrontAndBackListener listener : sAppFrontAndBackListenerList) {
                        listener.onAppForeground();
                    }
                }
            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {

            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {

            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {
                sActivityForegroundCount--;
                if (sActivityForegroundCount == 0) {
                    for (OnAppFrontAndBackListener listener : sAppFrontAndBackListenerList) {
                        listener.onAppBackground();
                    }
                }
            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {
                int index = sActivityInstanceStack.indexOf(activity);
                sActivityInstanceStack.remove(index);
                sActivityClassStack.remove(index);

            }
        });
    }

    //APP是否在前台
    public static boolean isAppForeground() {
        return sActivityForegroundCount > 0;
    }

    //返回栈顶activity的class
    public static Class<? extends Activity> getStackTopActivityClass() {
        try {
            return sActivityClassStack.peek();
        } catch (EmptyStackException e) {
            return null;
        }
    }

    //返回栈顶Activity实例
    public static Activity getStackTopActivityInstance() {
        return sActivityInstanceStack.peek();
    }

    //判断activity是否存在与栈内
    public static boolean isExistInActivityStack(Class<? extends Activity> activityClass) {
        return sActivityClassStack.search(activityClass) >= 0;
    }

    //获取栈中的activity
    @Nullable
    @SuppressWarnings("unchecked")
    public static <T extends Activity> T getActivityInstance(Class<T> activityClass) {
        for (int i = sActivityClassStack.size() - 1; i >= 0; i--) {
            if (activityClass.equals(sActivityClassStack.get(i))) {
                return (T) sActivityInstanceStack.get(i);
            }
        }
        return null;
    }

    public static void finishActivitiesInStackAbove(Class<? extends Activity> activityClass) {
        finishActivitiesInStackAbove(activityClass, false);
    }

    //finish该activity之上的activity
    public static void finishActivitiesInStackAbove(Class<? extends Activity> activityClass, boolean include) {
        int index = sActivityClassStack.indexOf(activityClass);
        if (index < 0) {
            return;
        }
        if (include) {
            index--;
        }
        for (int i = sActivityClassStack.size() - 1; i > index; i--) {
            Class c = sActivityClassStack.get(i);
            if (!c.equals(activityClass)) {
                sActivityInstanceStack.get(i).finish();
            } else if (include) {
                sActivityInstanceStack.get(i).finish();
            }
        }
    }

    //finish该activity之下的activity
    public static void finishActivitiesInStackBelow(Class<? extends Activity> activityClass) {
        int index = sActivityClassStack.indexOf(activityClass);
        if (index < 0) {
            return;
        }
        for (int i = 0; i < index; i++) {
            Class c = sActivityClassStack.get(i);
            if (!c.equals(activityClass)) {
                sActivityInstanceStack.get(i).finish();
            }
        }
    }

    public static void finishAllActivities() {
        for (Activity activity : sActivityInstanceStack) {
            activity.finish();
        }
    }

    public static synchronized void addAppFrontAndBackListener(OnAppFrontAndBackListener listener) {
        if (!sAppFrontAndBackListenerList.contains(listener)) {
            if (listener != null) {
                sAppFrontAndBackListenerList.add(listener);
            }
        }
    }

    public static synchronized void removeAppFrontAndBackListener(OnAppFrontAndBackListener listener) {
        sAppFrontAndBackListenerList.remove(listener);
    }


    public interface OnAppFrontAndBackListener {
        void onAppForeground();

        void onAppBackground();
    }
}
