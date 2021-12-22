package com.example.test;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.ViewHolder> {

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_photo;
        private TextView et_content;

        public ViewHolder(@NonNull View itemView, final OnItemClickEventListener itemClickListener) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View a_view) {
                    final int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        itemClickListener.onItemClick(position);
                    }

                }
            });

            iv_photo = itemView.findViewById(R.id.iv_photo);
            et_content = itemView.findViewById(R.id.et_content);
        }
    }


    //어댑터 시작
    private ArrayList<DetailItem> itemList;
    private Context context;
    private ViewHolder holder;

    private int mCheckedPosition = -1;

    public interface OnItemClickEventListener {
        void onItemClick(int a_position);
    }

    private OnItemClickEventListener mItemClickListener = new OnItemClickEventListener() {
        @Override
        public void onItemClick(int a_position) {
            notifyItemChanged(mCheckedPosition, null);
            mCheckedPosition = a_position;
            notifyItemChanged(a_position, null);
        }
    };


    public DetailAdapter(ArrayList<DetailItem> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public DetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_spot_detail, parent, false);
        holder = new DetailAdapter.ViewHolder(view, mItemClickListener);
        return holder;
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull DetailAdapter.ViewHolder holder, int position) {
        DetailItem item = itemList.get(position);

        int color;
        if (holder.getAdapterPosition() == mCheckedPosition) {
            color = ContextCompat.getColor(holder.itemView.getContext(), R.color.gray);
        } else {
            color = ContextCompat.getColor(holder.itemView.getContext(), android.R.color.transparent);
        }

        holder.itemView.setBackgroundColor(color);
        Log.e("TAG", "내용 : ?" + item.getIv_photo());
        if (item.getIv_photo().equals("") || item.getIv_photo() == null) {
            holder.iv_photo.setImageResource(R.drawable.iconfinder_gallery_image);
        } else {
            Glide.with(holder.iv_photo).load(Uri.parse(item.getIv_photo())).into(holder.iv_photo);
        }
        holder.et_content.setText(item.getEt_content());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public DetailItem getSelected() {
        if (mCheckedPosition > -1) {
            return itemList.get(mCheckedPosition);
        }
        return null;
    }

    public void clearSelected() {
        mCheckedPosition = -1;
    }
}
