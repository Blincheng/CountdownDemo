package com.mic.countdowndemo;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zhaocheng on 2017/3/27.
 */

public class CountdownTimeQueueManager extends BaseCountdownTimeQueueManager{
    private static CountdownTimeQueueManager manager;
    private CountdownTimeQueueManager(){}
    public static CountdownTimeQueueManager getInstance(){
        if (manager == null){
            manager = new CountdownTimeQueueManager();
            manager.initCountdownTimeQueueManager();
        }
        return manager;
    }
    @Override
    void initCountdownTimeQueueManager() {
        timeQueue = new ArrayList<>();
        timeTimer = new Timer(true);
        timeTask = new TimerTask() {
            @Override
            public void run() {
                countdownTimeQueue();
            }
        };
        timeTimer.schedule(timeTask,1000,1000);
    }

    @Override
    public CountdownTime addTime(int time, String id, CountdownTime.OnCountdownTimeListener listener) {
        CountdownTime countdownTime;
        if(timeQueue.size() >0)
            for(int i =0;i<timeQueue.size();i++){
                countdownTime = timeQueue.get(i);
                if(TextUtils.equals(countdownTime.getId(),id)){
                    countdownTime.setListener(listener);
                    return countdownTime;
                }
            }
        countdownTime = new CountdownTime(time,id,listener);
        timeQueue.add(countdownTime);
        return countdownTime;
    }

    @Override
    synchronized void countdownTimeQueue() {
        if(timeQueue != null&&timeQueue.size()>0){
            for(int i =0;i<timeQueue.size();i++){
                if(timeQueue.get(i).countdown())
                    i --;
            }
        }
    }
}
