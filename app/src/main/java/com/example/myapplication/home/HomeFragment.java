package com.example.myapplication.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dingcan.R;
import com.example.dingcan.tools.MenuInfo;
import com.example.dingcan.ui.db.SqlHellper;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class HomeFragment extends Fragment implements View.OnClickListener {

    private HomeRecycleAdapter homeRecycleAdapter;
    private RecyclerView recyclerView;

    private Handler handler;
    private RelativeLayout add_people_edit;

    private List<MenuInfo> menuInfos;
    private String LoginUserName;
    private SqlHellper sqlHellper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home, container, false);

        SharedPreferences sp = getActivity().getSharedPreferences("CurrentLogin", MODE_PRIVATE);
        LoginUserName = sp.getString("username", "none");

        menuInfos = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recycleView);
        homeRecycleAdapter = new HomeRecycleAdapter(getActivity(), menuInfos);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setAdapter(homeRecycleAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        /**nestedScroll嵌套recycleView，添加此段代码使滑动带惯性**/
        recyclerView.setNestedScrollingEnabled(false);

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                switch (message.what) {
                    case 0:
                        homeRecycleAdapter.notifyDataSetChanged();
                        break;
                }
                return false;
            }
        });

        add_people_edit = view.findViewById(R.id.add_people_edit);
        add_people_edit.setOnClickListener(this);

        sqlHellper = new SqlHellper(getActivity());
        initData();
        return view;
    }

    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                menuInfos.clear();

                SQLiteDatabase sqLiteDatabase = sqlHellper.getReadableDatabase();
                Cursor cursor = sqLiteDatabase.query("menu", null, null, null, null, null, null);


                if (cursor.moveToFirst()) {
                    do {
                        menuInfos.add(new MenuInfo(
                                cursor.getString(cursor.getColumnIndex("dishname")),
                                AboutIntroduction(cursor.getString(cursor.getColumnIndex("introduce"))),
                                cursor.getBlob(cursor.getColumnIndex("img")),
                                cursor.getString(cursor.getColumnIndex("identity")),
                                cursor.getString(cursor.getColumnIndex("distance")),
                                cursor.getString(cursor.getColumnIndex("price"))
                        ));
                    }
                    while (cursor.moveToNext());
                }

                cursor.close();
                sqLiteDatabase.close();
                Message message = Message.obtain();
                message.what = 0;
                handler.sendMessage(message);

            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_people_edit:
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public String AboutIntroduction(String str) {
        if (str.length() > 18) {
            str = str.substring(0, 18) + "...";

        }
        return str;
    }

}



