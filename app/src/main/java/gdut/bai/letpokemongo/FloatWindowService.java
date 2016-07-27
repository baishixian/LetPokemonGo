package gdut.bai.letpokemongo;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by baishixian on 2016/7/20.
 */
public class FloatWindowService extends Service {


    final String POKEMON_PACKAGE_NAME = "com.nianticlabs.pokemongo";

    /**
     * 用于在线程中创建或移除悬浮窗。
     */
    private Handler handler = new Handler();

    /**
     * 定时器，定时进行检测当前应该创建还是移除悬浮窗。
     */
    private Timer timer;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 开启定时器，每隔0.5秒刷新一次
        if (timer == null) {
            timer = new Timer();
            timer.scheduleAtFixedRate(new RefreshTask(), 0, 500);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Service被终止的同时也停止定时器继续运行
        timer.cancel();
        timer = null;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class RefreshTask extends TimerTask {

        @Override
        public void run() {
            // 当前界面是桌面，且没有悬浮窗显示，则创建悬浮窗。
            if (isPokemonApp() && !FloatWindowManager.isWindowShowing()) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        FloatWindowManager.createFloatWindow(getApplicationContext());
                    }
                });
            }
            // 当前界面不是桌面，且有悬浮窗显示，则移除悬浮窗。
            else if (!isPokemonApp() && FloatWindowManager.isWindowShowing()) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        FloatWindowManager.removeFloatWindow(getApplicationContext());
                    }
                });
            }
        }

    }

    /**
     * 判断当前界面是否是
     */
    private boolean isPokemonApp() {
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            final int PROCESS_STATE_TOP = 2;
            ActivityManager.RunningAppProcessInfo currentInfo = null;
            Field field = null;
            try {
                field = ActivityManager.RunningAppProcessInfo.class.getDeclaredField("processState");
            } catch (Exception ignored) {
            }
            ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> appList = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo app : appList) {
                if (app.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND &&
                        app.importanceReasonCode == 0 ) {
                    Integer state = null;
                    try {
                        state = field.getInt( app );
                    } catch (Exception ignored) {
                    }
                    if (state != null && state == PROCESS_STATE_TOP) {
                        currentInfo = app;
                        break;
                    }
                }
            }
            if (currentInfo.processName.equals(POKEMON_PACKAGE_NAME)){
                return true;
            }
        }else{
            ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> runningAppProcesses  = mActivityManager.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo  runningAppInfo : runningAppProcesses){
                String packageName = runningAppInfo.processName;
                if (packageName.equals(POKEMON_PACKAGE_NAME) && runningAppInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND){
                    Log.i("Pokemon","isPokemonApp true");
                    return true;
                }
            }
        }
        return false;*/
        return true;
    }
}
