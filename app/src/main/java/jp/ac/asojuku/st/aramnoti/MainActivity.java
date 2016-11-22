package jp.ac.asojuku.st.aramnoti;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;



public class MainActivity extends AppCompatActivity {

    private MyBroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mReceiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            //複数のインテントを受信する場合はif文を使う
            if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
                int scale = intent.getIntExtra("scale", 0);
                int level = intent.getIntExtra("level", 0);
                Activity mainActivity = (Activity) context;
                TextView tvTitle = (TextView) mainActivity.findViewById(R.id.tvTitle);

                //タイトル文章を設定
                String title = "BatteryNotice";
                tvTitle.setText(title);

                TextView tvMsg = (TextView) mainActivity.findViewById(R.id.tvMsg);

                //メッセージ文章を設定
                String msg = level + " / " + scale;
                tvMsg.setText(msg);

                if (level < 15) {
                    NotificationManager myNotification = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    Notification.Builder builder = new Notification.Builder(context);
                    builder.setSmallIcon(android.R.drawable.ic_dialog_info)
                            .setContentTitle("バッテリー残量")
                            .setContentText("バッテリー残量が15%になりました。")
                            .setPriority(Notification.PRIORITY_HIGH)
                            .setDefaults(Notification.DEFAULT_SOUND);
                    myNotification.notify(1, builder.build());
                }

            }
        }
    }

}


