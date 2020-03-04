package com.example.myapplication.circle;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.SqlHelper;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class CircleRecycleAdapter extends RecyclerView.Adapter<CircleRecycleAdapter.myHolder> {

    private Context context;
    private List<CircleInfo> menuInfos;

    public CircleRecycleAdapter(Context context, List<CircleInfo> menuInfos) {
        this.context = context;
        this.menuInfos = menuInfos;
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.circle_item, parent, false);
        final myHolder myHolder = new myHolder(view);


        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final myHolder myHolder, final int position) {

        myHolder.show_username.setText(menuInfos.get(position).username);
        myHolder.show_time.setText(menuInfos.get(position).time);
        myHolder.show_content.setText(menuInfos.get(position).content);
        myHolder.show_comment_number.setText("回答" + menuInfos.get(position).discuss_number);
        myHolder.click_show_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp = context.getSharedPreferences("CurrentLogin", MODE_PRIVATE);
                String LoginUserName = sp.getString("username", "none");
                Intent intent = new Intent(context, WriteAnswerActivity.class);
                //flag： 0：回复发帖人  1：回复其他的人
//                intent.putExtra("flag", 0);
                intent.putExtra("circle_id", menuInfos.get(position).id);
                intent.putExtra("reply_people_name", menuInfos.get(position).username);
                context.startActivity(intent);
            }
        });

        if (menuInfos.get(position).IsFav == 1) {
            myHolder.show_favourite.setImageResource(R.drawable.ic_favorite_black_24dp);
            myHolder.show_fav_state.setText("已收藏");
            myHolder.click_to_fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences sp = context.getSharedPreferences("CurrentLogin", MODE_PRIVATE);
                    String LoginUserName = sp.getString("username", "none");

                    SqlHelper myDatabaseHelper = new SqlHelper(context);
                    SQLiteDatabase sqLiteDatabase = myDatabaseHelper.getWritableDatabase();
                    sqLiteDatabase.delete("favourite", "information_id = ? and username = ?", new String[]{String.valueOf(menuInfos.get(position).id), LoginUserName});

                    menuInfos.get(position).IsFav = 0;
                    sqLiteDatabase.close();
                    notifyDataSetChanged();

                    Toast.makeText(context, "取消收藏！", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            myHolder.show_favourite.setImageResource(R.drawable.ic_favorite_border_red_200_24dp);
            myHolder.show_fav_state.setText("收藏");
            myHolder.click_to_fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences sp = context.getSharedPreferences("CurrentLogin", MODE_PRIVATE);
                    String LoginUserName = sp.getString("username", "none");

                    SqlHelper myDatabaseHelper = new SqlHelper(context);
                    SQLiteDatabase sqLiteDatabase = myDatabaseHelper.getWritableDatabase();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("information_id", menuInfos.get(position).id);
                    contentValues.put("username", LoginUserName);
                    sqLiteDatabase.insert("favourite", null, contentValues);

                    menuInfos.get(position).IsFav = 1;
                    sqLiteDatabase.close();

                    notifyDataSetChanged();
                    Toast.makeText(context, "已收藏！", Toast.LENGTH_LONG).show();
                }
            });
        }

        myHolder.ItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ShowDiscussAcitvity.class);
                intent.putExtra("id", menuInfos.get(position).id);
                intent.putExtra("reply_people_name", menuInfos.get(position).username);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return menuInfos.size();
    }

    class myHolder extends RecyclerView.ViewHolder {
        ImageView show_favourite;
        TextView show_username, show_time, show_content, show_comment_number, show_fav_state;
        View ItemView;
        LinearLayout click_to_fav, click_show_number;

        private myHolder(View itemView) {
            super(itemView);

            show_favourite = itemView.findViewById(R.id.show_favourite);
            show_username = itemView.findViewById(R.id.show_username);
            show_time = itemView.findViewById(R.id.show_time);
            show_content = itemView.findViewById(R.id.show_content);
            show_comment_number = itemView.findViewById(R.id.show_comment_number);
            click_to_fav = itemView.findViewById(R.id.click_to_fav);
            click_show_number = itemView.findViewById(R.id.click_show_number);
            show_fav_state = itemView.findViewById(R.id.show_fav_state);
            ItemView = itemView;
        }
    }

}
