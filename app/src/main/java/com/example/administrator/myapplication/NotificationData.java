package com.example.administrator.myapplication;

import android.content.Context;

/**
 * Created by Administrator on 26/10/2559.
 */
public class NotificationData {

    public static Context ctx = null;

    public Context getCtx() {
        return ctx;
    }

    public void setCtx(Context ctx) {
        this.ctx = ctx;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    String message;

    public String getMessage() {
        return message;
    }

    public NotificationData(String message) {
        this.message = message;
    }

    /*public static class Data {
        static String message;

        public static String getMessage() {
            if(message != null) {
                return message;
            } else {
                return "";
            }
        }

        public static void setMessage(String msg) {
            message = msg;
        }
    }*/

//    public static Data getInstance(String msg) {
//        return new Data(msg);
//    }
}
