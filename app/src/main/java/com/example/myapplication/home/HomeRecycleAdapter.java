package com.example.myapplication.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;

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
    public void onBindViewHolder(@NonNull final myHolder myHolder, final int i) {

        myHolder.show_menu.setText(menuInfos.get(i).title);
        myHolder.show_time.setText(menuInfos.get(i).time);

        myHolder.show_read.setText(menuInfos.get(i).read_number + "阅读");

        Bitmap imagebitmap = BitmapFactory.decodeByteArray(menuInfos.get(i).img, 0, menuInfos.get(i).img.length);
//        //将位图显示为图片
        myHolder.show_img.setImageBitmap(imagebitmap);


        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("flag", menuInfos.get(i).id);
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
