package com.example.myapplication.home;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.SqlHelper;

import java.util.List;

public class HomeRecycleAdapter extends RecyclerView.Adapter<HomeRecycleAdapter.myHolder> {

    private Context context;
    private List<NewsInfo> menuInfos;

    public HomeRecycleAdapter(Context context, List<NewsInfo> menuInfos) {
        this.context = context;
        this.menuInfos = menuInfos;
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item, parent, false);
        final myHolder myHolder = new myHolder(view);


        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final myHolder myHolder, final int position) {

        myHolder.show_menu.setText(menuInfos.get(position).title);
        myHolder.show_time.setText(menuInfos.get(position).time);

        myHolder.show_read.setText(menuInfos.get(position).read_number + "阅读");

        Bitmap imagebitmap = BitmapFactory.decodeByteArray(menuInfos.get(position).img, 0, menuInfos.get(position).img.length);
//        //将位图显示为图片
        myHolder.show_img.setImageBitmap(imagebitmap);

        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SqlHelper sqlHelper = new SqlHelper(context);
                SQLiteDatabase sqLiteDatabase = sqlHelper.getWritableDatabase();
                Cursor cursor1 = sqLiteDatabase.query("information", null, "id = ?", new String[]{String.valueOf(menuInfos.get(position).id)},null,null,null);
                int count = 0;
                if (cursor1.moveToFirst())
                {
                    count = cursor1.getInt(cursor1.getColumnIndex("read_number"));
                }
                count++;

                ContentValues values = new ContentValues();
                values.put("read_number", count);
                sqLiteDatabase.update("information", values, "id = ?", new String[]{String.valueOf(menuInfos.get(position).id)});
                cursor1.close();
                sqLiteDatabase.close();

                Intent intent0 = new Intent();
                intent0.setAction("PUBLISHINFO");
                context.sendBroadcast(intent0);

                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("flag", menuInfos.get(position).id);
                context.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return menuInfos.size();
    }

    class myHolder extends RecyclerView.ViewHolder {
        ImageView show_img;
        TextView show_menu, show_time, show_read;
        View ItemView;

        private myHolder(View itemView) {
            super(itemView);

            show_img = itemView.findViewById(R.id.show_img);
            show_menu = itemView.findViewById(R.id.show_menu);
            show_time = itemView.findViewById(R.id.show_time);
            show_read = itemView.findViewById(R.id.show_read);
            ItemView = itemView;
        }
    }

}
