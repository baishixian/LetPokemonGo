package gdut.bai.letpokemongo;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * Created by baishixian on 2016/7/20.
 */
public class FloatWindowView extends LinearLayout{
    /**
     * 记录悬浮窗的宽度
     */
    public static int viewWidth;

    /**
     * 记录悬浮窗的高度
     */
    public static int viewHeight;

    public FloatWindowView(final Context context) {
        super(context);
        LocationUtil.setContext(context);
       // LayoutInflater.from(context).inflate(R.layout.layout_float_controller, this);
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.layout_float_controller, this);
        RelativeLayout view = (RelativeLayout)findViewById(R.id.layout_float);
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;

        findViewById(R.id.imageView_up).setOnClickListener(onClickListener);
        findViewById(R.id.imageView_left).setOnClickListener(onClickListener);
        findViewById(R.id.imageView_down).setOnClickListener(onClickListener);
        findViewById(R.id.imageView_right).setOnClickListener(onClickListener);


    }

    View.OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.imageView_up:
                    Log.i("let pokemon","move up");
                    LocationUtil.moveUp();
                    break;
                case R.id.imageView_left:
                    Log.i("let pokemon","move left");
                    LocationUtil.moveLeft();
                    break;
                case R.id.imageView_down:
                    Log.i("let pokemon","move down");
                    LocationUtil.moveDown();
                    break;
                case R.id.imageView_right :
                    Log.i("let pokemon","move right");
                    LocationUtil.moveRight();
                    break;
                default: break;
            }
        }
    };
}
