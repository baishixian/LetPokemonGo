package gdut.bai.letpokemongo;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * Created by baishixian on 2016/7/20.
 */
public class FloatWindowManager {

    /**
     * 悬浮窗View的实例
     */
    private static FloatWindowView floatWindowView;


    /**
     * 悬浮窗View的参数
     */
    private static WindowManager.LayoutParams windowParams;

    /**
     * 用于控制在屏幕上添加或移除悬浮窗
     */
    private static WindowManager mWindowManager;

    /**
     * 用于获取手机可用内存
     */
    private static ActivityManager mActivityManager;


    /**
     * 创建一个悬浮窗。位置为屏幕左上角。
     *
     * @param context
     *            必须为应用程序的Context.
     */
    public static void createFloatWindow(Context context) {
        WindowManager windowManager = getWindowManager(context);
        if (floatWindowView == null) {
            floatWindowView = new FloatWindowView(context);

            windowParams = new WindowManager.LayoutParams(150,150);
            windowParams.type = WindowManager.LayoutParams.TYPE_PHONE;
            windowParams.format = PixelFormat.RGBA_8888;
            windowParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            windowParams.gravity = Gravity.LEFT | Gravity.BOTTOM;
            windowParams.width = FloatWindowView.viewWidth;
            windowParams.height = FloatWindowView.viewHeight;
            floatWindowView.setLayoutParams(windowParams);
            windowManager.addView(floatWindowView, windowParams);
        }
    }

    /**
     * 将悬浮窗从屏幕上移除。
     *
     * @param context
     *            必须为应用程序的Context.
     */
    public static void removeFloatWindow(Context context) {
        if (floatWindowView != null) {
            WindowManager windowManager = getWindowManager(context);
            windowManager.removeView(floatWindowView);
            floatWindowView = null;
        }
    }


    /**
     * 是否有悬浮窗显示在屏幕上。
     *
     * @return 有悬浮窗显示在桌面上返回true，没有的话返回false。
     */
    public static boolean isWindowShowing() {
        return floatWindowView != null && floatWindowView.getVisibility() == View.VISIBLE;
    }

    /**
     * 如果WindowManager还未创建，则创建一个新的WindowManager返回。否则返回当前已创建的WindowManager。
     *
     * @param context
     *            必须为应用程序的Context.
     * @return WindowManager的实例，用于控制在屏幕上添加或移除悬浮窗。
     */
    private static WindowManager getWindowManager(Context context) {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManager;
    }

    /**
     * 如果ActivityManager还未创建，则创建一个新的ActivityManager返回。否则返回当前已创建的ActivityManager。
     *
     * @param context
     *            可传入应用程序上下文。
     * @return ActivityManager的实例，用于获取手机可用内存。
     */
    private static ActivityManager getActivityManager(Context context) {
        if (mActivityManager == null) {
            mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        }
        return mActivityManager;
    }

}
