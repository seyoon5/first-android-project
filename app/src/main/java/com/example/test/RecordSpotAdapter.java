package com.example.test;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class
RecordSpotAdapter extends RecyclerView.Adapter<RecordSpotAdapter.ViewHolder> {


    public int getSelectedItem(SpotItem recyclerItem) {
        return mCheckedPosition;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageButton ib_Rphoto;
        TextView tv_Rname;
        TextView tv_Rspot;
        RatingBar ratingBar; //edit

        public ViewHolder(@NonNull View itemView, final OnItemClickEventListener a_itemClickListener) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View a_view) {
                    Log.e("ViewHolder", "onClick1");
                    final int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        a_itemClickListener.onItemClick(position);
                    }
                }
            });
            Log.e("ViewHolder", "findview");
            ib_Rphoto = itemView.findViewById(R.id.ib_Rphoto);
            tv_Rname = itemView.findViewById(R.id.tv_Rname);
            tv_Rspot = itemView.findViewById(R.id.tv_Rspot);
            ratingBar = itemView.findViewById(R.id.rating);

        /*    ib_Rphoto.setOnClickListener(v -> {
                int key = getBindingAdapterPosition();
                Context context = v.getContext();
                Intent intent = new Intent(v.getContext(), SpotDetail.class);
                intent.putExtra("key", key);
                context.startActivity(intent);
            });*/
        }
    }

    //어댑터 시작
    private ArrayList<SpotItem> mItemList;

    //어댑터 생성자
    public RecordSpotAdapter(ArrayList<SpotItem> itemList) {
        this.mItemList = itemList;
    }

    public interface OnItemClickEventListener {
        void onItemClick(int a_position);
    }

    private OnItemClickEventListener mItemClickListener = new OnItemClickEventListener() {
        @Override
        public void onItemClick(int a_position) {
            Log.e("onItemClick", "1");

            notifyItemChanged(mCheckedPosition, null);
            System.out.println(mCheckedPosition);

            mCheckedPosition = a_position;
            System.out.println(mCheckedPosition);

            notifyItemChanged(a_position, null);

            Log.e("onItemClick", "2");
        }
    };

    private int mCheckedPosition = -1;


    @Override
    public RecordSpotAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.e("onCreateViewHolder", "1");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_spot, parent, false);
        Log.e("onCreateViewHolder", "2");
        ViewHolder holder = new RecordSpotAdapter.ViewHolder(view, mItemClickListener);
        Log.e("onCreateViewHolder", "3");
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int a_position) {
        SpotItem items = mItemList.get(a_position);

        holder.ib_Rphoto.setTag(a_position);
        holder.ib_Rphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int k = (int) v.getTag();
                int key = k;
                Context context = v.getContext();
                Intent intent = new Intent(v.getContext(), SpotDetail.class);
                intent.putExtra("key", key);
                context.startActivity(intent);
            }
        });


        int color;

        if (holder.getBindingAdapterPosition() == mCheckedPosition) {
            color = ContextCompat.getColor(holder.itemView.getContext(), R.color.gray);
        } else {
            color = ContextCompat.getColor(holder.itemView.getContext(), android.R.color.transparent);
        }
        holder.itemView.setBackgroundColor(color);
        if (!items.getIv_Rphoto().equals("")) {
            Glide.with(holder.ib_Rphoto.getContext()).load(Uri.parse(items.getIv_Rphoto())).into(holder.ib_Rphoto);
        } else if (items.getIv_Rphoto().equals("")) {
            holder.ib_Rphoto.setImageResource(R.drawable.ic_launcher_slack_home);
        }
        holder.tv_Rname.setText(items.getTv_Rname());
        holder.tv_Rspot.setText(items.getTv_Rspot());
        try {
            holder.ratingBar.setRating(Float.parseFloat(items.getTv_Rrate())); //edit
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    public SpotItem getSelected() {
        if (mCheckedPosition > -1) {
            return mItemList.get(mCheckedPosition);
        }
        return null;
    }

    public void clearSelected() {
        mCheckedPosition = -1;
    }
}
