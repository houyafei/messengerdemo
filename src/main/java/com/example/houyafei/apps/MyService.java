package com.example.houyafei.apps;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class MyService extends Service {

    public static final int MSG_FROM_CLIENT = 0x909090;
    public static final int MSG_FROM_SERVICE = 0x9090901;

    /**
     * class
     * 创建一个Handler类，接受消息
     */
    private static class MessengerHandler extends Handler{


        private static final String TAG = "MyService";

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_FROM_CLIENT:
                    Log.i(TAG,"this is the msg from client :"+msg.getData().getString("msg"));

                    //信使
                    Messenger messenger = msg.replyTo;
                    //信息载体
                    Message message = Message.obtain(null,MyService.MSG_FROM_SERVICE);
                    //数据
                    Bundle bundle = new Bundle();
                    bundle.putString("reply","我很开心");
                    message.setData(bundle);
                    try {
                        messenger.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;

                default:
                    super.handleMessage(msg);
            }
        }
    }

    /**
     * 创建Messenger对象，该对象持有Handler对象
     */
    private final Messenger mMessenger = new Messenger(new MessengerHandler());

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        return mMessenger.getBinder();
    }


}
