package com.example.administrator.myapplication;

/**
 * Created by Administrator on 30/9/2559.
 */
public class ParallelDataOne {
    private int minute;
    private int hour;

    public ParallelDataOne() {
    }

    public ParallelDataOne(int minute, int hour) {
        this.minute = minute;
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }
}
