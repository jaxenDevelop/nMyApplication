package com.example.myapplication.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

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


public class HomeFragment extends Fragment{

    private HomeRecycleAdapter homeRecycleAdapter;
    private RecyclerView recyclerView;
    private ImageView add0;
    private List<NewsInfo> menuInfos;
    private ReceiveMessage receiveMessage;
    private IntentFilter intentFilter;
    private SqlHelper sqlHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home, container, false);

        menuInfos = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recycleView);
        add0 = view.findViewById(R.id.add0);
        add0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(getActivity(), CreateMenuActivity.class);
                startActivity(intent2);
            }
        });
        homeRecycleAdapter = new HomeRecycleAdapter(getActivity(), menuInfos);
        sqlHelper = new SqlHelper(getActivity());
        initData();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setAdapter(homeRecycleAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        /**nestedScroll嵌套recycleView，添加此段代码使滑动带惯性**/
        recyclerView.setNestedScrollingEnabled(false);

        receiveMessage = new ReceiveMessage();
        intentFilter = new IntentFilter();
        intentFilter.addAction("PUBLISHINFO");
        getActivity().registerReceiver(receiveMessage, intentFilter);

        return view;
    }

    private void initData() {
        menuInfos.clear();
        SQLiteDatabase sqLiteDatabase = sqlHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query("information", null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                menuInfos.add(new NewsInfo(
                        cursor.getInt(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex("editor")),
                        cursor.getString(cursor.getColumnIndex("title")),
                        cursor.getString(cursor.getColumnIndex("time")),
                        cursor.getBlob(cursor.getColumnIndex("img")),
                        cursor.getString(cursor.getColumnIndex("read_number")),
                        cursor.getString(cursor.getColumnIndex("content"))
                ));
            }
            while (cursor.moveToNext());
        }

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
            homeRecycleAdapter.notifyDataSetChanged();
        }
    }
}



