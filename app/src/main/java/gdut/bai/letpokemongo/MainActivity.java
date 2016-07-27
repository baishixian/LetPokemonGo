package gdut.bai.letpokemongo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int ACCESS_FINE_LOCATION_RESULT_CODE = 10;
    private Button control_button;
    private boolean isControlShowing = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        requestLocationPermission();
    }

    private void initView() {
        control_button = (Button) findViewById(R.id.control_button);
        control_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.control_button://管理悬浮控件
                setControlState();
                break;
            default: break;

        }
    }

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION_RESULT_CODE);
        }
    }

    private void setControlState() {
        if (!isControlShowing){//如果悬浮控件没有显示
            showControlWindow();
        }else{
            cancelControlWindow();
            isControlShowing = false;
            control_button.setText("开启悬浮控件");
        }
    }

    private void cancelControlWindow() {
        // 点击关闭悬浮窗的时候，移除所有悬浮窗，并停止Service
        FloatWindowManager.removeFloatWindow(MainActivity.this);
        Intent intent = new Intent(MainActivity.this, FloatWindowService.class);
        MainActivity.this.stopService(intent);
    }

    private void showControlWindow() {
        if(Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(MainActivity.this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 1234);
            }else{
                startFloatWindowService();
            }
        }
        else
        {
            startFloatWindowService();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1234 && resultCode == RESULT_OK){
            startFloatWindowService();
        }else{
            Toast.makeText(MainActivity.this,"请授予显示悬浮控制的权限",Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void startFloatWindowService(){
        Intent intent = new Intent(MainActivity.this, FloatWindowService.class);
        startService(intent);
        isControlShowing = true;
        control_button.setText("关闭悬浮控件");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doNext(requestCode,grantResults);
    }

    private void doNext(int requestCode, int[] grantResults) {
        if (requestCode == ACCESS_FINE_LOCATION_RESULT_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                Toast.makeText(MainActivity.this,"授予模拟位置所需的权限成功",Toast.LENGTH_SHORT).show();
            } else {
                // Permission Denied
                Toast.makeText(MainActivity.this,"请授予模拟位置所需的权限",Toast.LENGTH_SHORT).show();
                requestLocationPermission();
            }
        }
    }
}
