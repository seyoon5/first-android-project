package com.example.test;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {

    List<FriendsItem> itemList;

    public FriendsAdapter(){

    }
    public FriendsAdapter(List<FriendsItem> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public FriendsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_friends_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FriendsAdapter.ViewHolder holder, int position) {
        holder.videoWeb.loadData(itemList.get(position).getVideoUrl(), "text/html", "utf-8");
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        WebView videoWeb;

        public ViewHolder(View itemView) {
            super(itemView);

            videoWeb = itemView.findViewById(R.id.videoWebView_friends);

            videoWeb.getSettings().setJavaScriptEnabled(true);
            videoWeb.setWebChromeClient(new WebChromeClient(){

            });
        }
    }
}
