package com.example.test;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchHomeAdapter extends RecyclerView.Adapter<SearchHomeAdapter.ViewHolder> {

    private ArrayList<SearchHomeItem> mData;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_searchResult_title, search_description, search_link;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_searchResult_title = itemView.findViewById(R.id.search_title);
            search_description = itemView.findViewById(R.id.search_description);
            search_link = itemView.findViewById(R.id.search_link);
            // 뷰 객체에 대한 참조.
        }
    }

    //생성자에서 데이터 리스트 객체를 전달받음.
    public SearchHomeAdapter(Context context, ArrayList<SearchHomeItem> list) {
        this.context = context;
        this.mData = list;
    }


    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴
    @Override
    public SearchHomeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.activity_search_item, parent, false);
        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_search_result, parent, false);
        SearchHomeAdapter.ViewHolder vh = new SearchHomeAdapter.ViewHolder(view);

        return vh;
    }

    // onBindViewHolder() - position 에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    @Override
    public void onBindViewHolder(SearchHomeAdapter.ViewHolder holder, int position) {
        SearchHomeItem text = mData.get(position);

        Log.e("TAG", "내용 :text  " + text.getTitle());
        holder.tv_searchResult_title.setText(mData.get(position).getTitle());
        holder.search_description.setText(mData.get(position).getDescription());
        holder.search_link.setText(mData.get(position).getLink());
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size();
    }


}

