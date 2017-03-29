package com.yiguo.countdowndemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    CountdownTime.OnTimeCountdownOverListener listener;
    private ArrayList<Integer> data;
    private int now;
    private int[] time = {5000,202,220,201,230,240,241,250,231,300,
            80,70,60,50,40,30,20,10,500,400};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        data = new ArrayList<>();
        for(int i = 0;i<20;i++){
            data.add(time[i]);
        }
        now = (int) (System.currentTimeMillis()/1000);
        CountdownTime.onTimeCountdownOverListener = new CountdownTime.OnTimeCountdownOverListener() {
            @Override
            public void onTimeCountdownOver() {
                Log.d("Blin QueueMangerOver","回调了");
            }
        };
        RecyclerView list = (RecyclerView) findViewById(R.id.list);
        //设置布局管理器
        list.setLayoutManager(new LinearLayoutManager(this));
        //设置Adapter
        list.setAdapter(new MyRecyclerAdapter());

    }
    class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder>{
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder viewHolder =
                    new MyViewHolder(LayoutInflater.from(MainActivity.this).inflate(
                            R.layout.item,parent,false));
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.tv.setCountdownTime(data.get(position)-((int) (System.currentTimeMillis()/1000)-now),data.get(position)+"");
            holder.tv2.setText("总倒计时"+data.get(position)+":");
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
        class MyViewHolder extends RecyclerView.ViewHolder{
            public CountdownView tv;
            public TextView tv2;
            public MyViewHolder(View itemView) {
                super(itemView);
                tv = (CountdownView) itemView.findViewById(R.id.countdownView);
                tv2 = (TextView) itemView.findViewById(R.id.tv);
            }
        }
    }
}
