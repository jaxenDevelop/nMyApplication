package com.example.myapplication.circle;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.List;

public class DiscussRecycleAdapter extends RecyclerView.Adapter<DiscussRecycleAdapter.myHolder> {

    private Context context;
    private String circle_username;
    private List<DiscussInfo> menuInfos;

    public DiscussRecycleAdapter(Context context, List<DiscussInfo> menuInfos, String circle_username) {
        this.context = context;
        this.menuInfos = menuInfos;
        this.circle_username = circle_username;
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.discuss_item, parent, false);
        final myHolder myHolder = new myHolder(view);


        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final myHolder myHolder, final int position) {

        myHolder.show_username.setText(menuInfos.get(position).username);
        myHolder.show_time.setText(menuInfos.get(position).time);
        if (menuInfos.get(position).reply_people_name.equals(circle_username))
            myHolder.show_reply.setVisibility(View.GONE);
        else {
            myHolder.show_reply.setVisibility(View.VISIBLE);
            myHolder.show_reply.setText("回复@" + menuInfos.get(position).reply_people_name + ":");
        }

        myHolder.show_discuss.setText(menuInfos.get(position).content);

        myHolder.reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, WriteAnswerActivity.class);
//                intent.putExtra("flag", 1);
                intent.putExtra("reply_people_name", menuInfos.get(position).username);
                intent.putExtra("circle_id", menuInfos.get(position).circle_id);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return menuInfos.size();
    }

    class myHolder extends RecyclerView.ViewHolder {
        TextView show_username, show_time, show_reply, show_discuss, reply;
        View ItemView;

        private myHolder(View itemView) {
            super(itemView);

            show_reply = itemView.findViewById(R.id.show_reply);
            show_username = itemView.findViewById(R.id.show_username);
            show_time = itemView.findViewById(R.id.show_time);
            show_discuss = itemView.findViewById(R.id.show_discuss);
            reply = itemView.findViewById(R.id.reply);
            ItemView = itemView;
        }
    }

}
