package com.lib.weight.free_recyclerview;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.lib.weight.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by myt on 2017/5/30.
 */
public class TabAdapter extends RecyclerView.Adapter<TabAdapter.AdapterViewHolder>{

    private List<Data> mDatas;
    private String[]   mTitles;

    private RecyclerView recyclerView;

    public TabAdapter( RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public void setDatas(List<Data> datas, String []titles) {
        mDatas = datas;
        mTitles = titles;
        notifyDataSetChanged();
    }

    @Override
    public AdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new AdapterViewHolder(View.inflate(parent.getContext(), R.layout.wide_item, null));
    }

    @Override
    public void onBindViewHolder(AdapterViewHolder holder, int position) {
        Data data = mDatas.get(position);

//        holder.textView.setTextAppearance(CustomApplication.getContext(),R.style.market_list_content_txt);
        holder.textView.setText(data.getName());

        int count = holder.tab_tv.length;

        for (int i = 0;i< count;i++){
            TextView v = holder.tab_tv[i];
            String str = data.getArray()[i];
            v.setText(str);
        }
    }

    @Override
    public int getItemCount() {

        if (mDatas != null && mDatas.size()> 0) {
            return mDatas.size();
        }

        return 0;
    }


    public class AdapterViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        AnimateScrollView anScrView;
        LinearLayout tab_root,ll_content;
        TextView[] tab_tv;
        public AdapterViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv);
            ll_content = (LinearLayout) itemView.findViewById(R.id.ll_content);
            ll_content.setVisibility(View.GONE);
            anScrView = (AnimateScrollView) itemView.findViewById(R.id.scrollView);
            tab_root = (LinearLayout) itemView.findViewById(R.id.tab_root);
            anScrView.setTag(recyclerView);

            tab_tv = new TextView[mTitles.length];
            addRow(tab_root,tab_tv,mTitles.length);
        }

    }

    private void addRow(LinearLayout linearLayout, TextView[] textViews, int size){
        linearLayout.removeAllViews();
        for (int i = 0; i < size; i++){
            View v = View.inflate(linearLayout.getContext(), R.layout.wide_table,null);
            TextView con =  (TextView) v.findViewById(R.id.wide_content);
//            con.setTextAppearance(CustomApplication.getContext(),R.style.market_list_content_txt);
            textViews[i] = con;
            linearLayout.addView(v);
        }
    }

}
