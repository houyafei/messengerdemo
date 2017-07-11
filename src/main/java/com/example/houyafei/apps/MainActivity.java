package com.example.houyafei.apps;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {


    private Messenger messenger ;

    private Messenger receiverMessenger = new Messenger(new ClientMessengerHandler());

    private ServiceConnection  connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            //信使
            messenger = new Messenger(iBinder);
                //信息载体
                Message message = Message.obtain(null,MyService.MSG_FROM_CLIENT);
                    //信息内容，即数据
                    Bundle bundle = new Bundle() ;
                    bundle.putString("msg","你好，我来自美丽的星星，yeah！");
                message.setData(bundle);

            //8888888888888888888，未接收数据而修改信息回应的信使
            message.replyTo = receiverMessenger ;
            //8888888888888888888
            try {
                messenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };


    /**
     * class
     * 信息处理的Handler的子类
     *
     */
    private static final class ClientMessengerHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MyService.MSG_FROM_SERVICE:
                    Log.i("TAG","来此服务端的消息回复："+msg.getData().getString("reply"));
                    break;
            }

        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this,MyService.class);
        bindService(intent,connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unbindService(connection);

    }
}
