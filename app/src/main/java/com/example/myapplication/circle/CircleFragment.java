package com.example.myapplication.circle;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.SqlHelper;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class CircleFragment extends Fragment {

    private CircleRecycleAdapter circleRecycleAdapter;
    private RecyclerView circle_view;
    private ImageView add_circle;
    private List<CircleInfo> menuInfos;
    private ReceiveMessage receiveMessage;
    private IntentFilter intentFilter;
    private SqlHelper sqlHelper;
    private String LoginUserName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.circle, container, false);

        SharedPreferences sp = getActivity().getSharedPreferences("CurrentLogin", MODE_PRIVATE);
        LoginUserName = sp.getString("username", "none");
        menuInfos = new ArrayList<>();
        circle_view = view.findViewById(R.id.circle_view);
        add_circle = view.findViewById(R.id.add_circle);
        add_circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(getActivity(), WriteCircleActivity.class);
                startActivity(intent2);
            }
        });
        circleRecycleAdapter = new CircleRecycleAdapter(getActivity(), menuInfos);
        sqlHelper = new SqlHelper(getActivity());
        initData();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        circle_view.setAdapter(circleRecycleAdapter);
        circle_view.setLayoutManager(linearLayoutManager);
        /**nestedScroll嵌套recycleView，添加此段代码使滑动带惯性**/
        circle_view.setNestedScrollingEnabled(false);

        receiveMessage = new ReceiveMessage();
        intentFilter = new IntentFilter();
        intentFilter.addAction("AddComment");
        getActivity().registerReceiver(receiveMessage, intentFilter);
        return view;
    }

    private void initData() {
        menuInfos.clear();
        SQLiteDatabase sqLiteDatabase = sqlHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query("comment", null, null, null, null, null, "id desc");

        if (cursor.moveToFirst()) {
            do {
                menuInfos.add(new CircleInfo(
                        cursor.getInt(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex("username")),
                        cursor.getString(cursor.getColumnIndex("time")),
                        cursor.getString(cursor.getColumnIndex("content")),
                        0,
                        cursor.getInt(cursor.getColumnIndex("discuss_number"))
                ));
            }
            while (cursor.moveToNext());
        }

        //判断帖子有没有点收藏（1：已收藏 0：未收藏）
        Cursor cursor1 = null;
        for (int i = 0; i < menuInfos.size(); i++) {
            cursor1 = sqLiteDatabase.query("favourite", null, "information_id = ? and username = ?", new String[]{String.valueOf(menuInfos.get(i).id), LoginUserName}, null, null, null);
            if (cursor1.moveToFirst()) {
                menuInfos.get(i).IsFav = 1;
            }
        }
        if (cursor1 != null)
            cursor1.close();
        cursor.close();
        sqLiteDatabase.close();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiveMessage);
    }

    public class ReceiveMessage extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            initData();
            circleRecycleAdapter.notifyDataSetChanged();
        }
    }
}



