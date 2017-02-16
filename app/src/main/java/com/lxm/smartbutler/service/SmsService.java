package com.lxm.smartbutler.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lxm.smartbutler.R;
import com.lxm.smartbutler.utils.L;
import com.lxm.smartbutler.utils.StaticClass;
import com.lxm.smartbutler.view.DispatchLinearLayout;

public class SmsService extends Service implements View.OnClickListener{

    private SmsReciver reciver;
    private String mPhone;
    private String mContent;

    private WindowManager vm;
    private WindowManager.LayoutParams layoutParams;
    private DispatchLinearLayout mView;
    private HomeWatchReceiver homeWatchReceiver;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        //注册短信监听广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(StaticClass.SMS_ACTION);
        filter.setPriority(Integer.MAX_VALUE);
        reciver = new SmsReciver();
        registerReceiver(reciver,filter);

        homeWatchReceiver = new HomeWatchReceiver();
        IntentFilter filter1 = new IntentFilter();
        filter1.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        registerReceiver(homeWatchReceiver,filter1);

        L.i("开启短信服务");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //取消短信监听广播
        unregisterReceiver(reciver);
        unregisterReceiver(homeWatchReceiver);
        L.i("关闭短信服务");
    }

    class SmsReciver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(StaticClass.SMS_ACTION)){
                L.i("收到短信了");
                Object[] objs = (Object[]) intent.getExtras().get("pdus");
                for (Object obj : objs) {
                    SmsMessage message = SmsMessage.createFromPdu((byte[]) obj);
                    mPhone = message.getOriginatingAddress();
                    mContent = message.getMessageBody();
                    L.i("mPhone:"+mPhone + "  mContent" + mContent);
                    showWindow();
                }
            }
        }
    }

    private void showWindow() {
        vm = (WindowManager) getSystemService(WINDOW_SERVICE);
        layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                |WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        layoutParams.format = PixelFormat.TRANSLUCENT;
        layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;

        mView = (DispatchLinearLayout) View.inflate(getApplicationContext(), R.layout.sms_item,null);
        mView.setmDispatchKeyEventListener(new DispatchLinearLayout.DispatchKeyEventListener() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                    if (mView.getParent() != null) {
                        vm.removeView(mView);
                    }
                    return true;
                }
                return false;
            }
        });

        vm.addView(mView,layoutParams);

        TextView tv_phone = (TextView) mView.findViewById(R.id.tv_phone);
        tv_phone.setText(mPhone);
        TextView tv_content = (TextView) mView.findViewById(R.id.tv_content);
        tv_content.setText(mContent);
        Button btn_send_sms = (Button) mView.findViewById(R.id.btn_send_sms);
        btn_send_sms.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send_sms:
                Uri uri = Uri.parse("smsto:" + mPhone);
                Intent intent = new Intent(Intent.ACTION_SENDTO,uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("sms_body", "");
                startActivity(intent);
                if (mView.getParent() != null) {
                    vm.removeView(mView);
                }
                break;
        }
    }

    //home监听
    private class HomeWatchReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() == Intent.ACTION_CLOSE_SYSTEM_DIALOGS) {

                String reason = intent.getStringExtra("reason");
                if (reason.equals("homekey")) {
                    if (mView.getParent() != null) {
                        vm.removeView(mView);
                    }
                }
            }
        }
    }
}
