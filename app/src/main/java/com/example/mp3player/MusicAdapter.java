package com.example.mp3player;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder> {

    ArrayList<AudioModel> list;
    Context mContext;

    public MusicAdapter(ArrayList<AudioModel> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.card_music, parent, false);
        return new MusicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {

        AudioModel songData = list.get(position);
        holder.textViewFileName.setText(songData.getTitle());

        if (MyMediaPlayer.currentIndex == position){
            holder.textViewFileName.setTextColor(Color.parseColor("#228B22"));
        }else {
            holder.textViewFileName.setTextColor(Color.parseColor("#000000"));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // navigate to another activity
                //create singleton class MyMediaPlayer
                MyMediaPlayer.getInstance().reset();
                MyMediaPlayer.currentIndex = holder.getAdapterPosition();
                Intent intent = new Intent(mContext, MusicActivity.class);
                intent.putExtra("List", list);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });

           /* String filePath = list.get(holder.getAdapterPosition());
            Log.e("filepath : ", filePath);
            String title = filePath.substring(filePath.lastIndexOf("/")+1);
            holder.textViewFileName.setText(title);
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, MusicActivity.class);
                    intent.putExtra("title", title);
                    intent.putExtra("filepath", filePath);
                    intent.putExtra("position", holder.getAdapterPosition());
                    intent.putExtra("list", list);

                    mContext.startActivity(intent);
                }
            });*/
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MusicViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewFileName;
        private ImageView imageView;
        //private CardView cardView;

        public MusicViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewFileName = itemView.findViewById(R.id.textViewFilename);
            imageView = itemView.findViewById(R.id.icon);
            //cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
