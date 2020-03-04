package com.example.myapplication.mine;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.SqlHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;


public class MineFragment extends Fragment implements View.OnClickListener {

    private TextView show_username, show_rgs_time, show_j;
    private Handler handler;
    private String LoginUserName;
    private String time;
    private AppCompatImageView edit_j;
    private String StrEdit;
    private RelativeLayout line0, line1, line2;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mine, container, false);

        show_j = view.findViewById(R.id.show_j);
        edit_j = view.findViewById(R.id.edit_j);
        edit_j.setOnClickListener(this);
        show_username = view.findViewById(R.id.show_username);
        show_rgs_time = view.findViewById(R.id.show_rgs_time);
        line0 = view.findViewById(R.id.line0);
        line1 = view.findViewById(R.id.line1);
        line2 = view.findViewById(R.id.line2);
        line0.setOnClickListener(this);
        line1.setOnClickListener(this);
        line2.setOnClickListener(this);

        SharedPreferences sp = getActivity().getSharedPreferences("CurrentLogin", MODE_PRIVATE);
        LoginUserName = sp.getString("username", "none");
        show_username.setText(LoginUserName);

        initData();

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                if (message.what == 1) {
                    show_j.setText(StrEdit);
                }
                return false;
            }
        });


        return view;
    }

    private void initData() {
        SqlHelper sqlHelper = new SqlHelper(getActivity());
        SQLiteDatabase db = sqlHelper.getReadableDatabase();
        Cursor cursor = db.query("userInfo", null, "username = ?", new String[]{LoginUserName}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                time = cursor.getString(cursor.getColumnIndex("time"));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        SharedPreferences sp = getActivity().getSharedPreferences("JianJie", MODE_PRIVATE);
        StrEdit = sp.getString("text", "添加个人简介，让朋友更了解你");

        show_rgs_time.setText(time + "加入");
        show_j.setText(StrEdit);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_j:
                SharedPreferences sp = getActivity().getSharedPreferences("JianJie", MODE_PRIVATE);
                String text = sp.getString("text", "添加个人简介，让朋友更了解你");
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                final EditText editText = new EditText(getActivity());
                editText.setHint(text);
                editText.setTextSize(17);
                builder.setTitle("设置个人简介").setView(editText);
                builder.setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences sp1 = getActivity().getSharedPreferences("JianJie", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp1.edit();
                        editor.putString("text", editText.getText().toString());
                        editor.apply();

                        StrEdit = editText.getText().toString();

                        Message message = Message.obtain();
                        message.what = 1;
                        handler.sendMessage(message);
                    }
                }).show();
                break;

            case R.id.line0:
                Intent intent = new Intent(getActivity(), ApplicationSpecialistActivity.class);
                startActivity(intent);
                break;

            case R.id.line1:
                Intent intent1 = new Intent(getActivity(), MessageActivity.class);
                startActivity(intent1);
                break;

            case R.id.line2:

                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    public String stampToDate(long timeMillis) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(timeMillis);
        return simpleDateFormat.format(date);
    }

}



