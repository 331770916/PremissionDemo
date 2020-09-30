package com.lib.weight.free_recyclerview;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.lib.weight.R;

import androidx.collection.SparseArrayCompat;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by Administrator on 2017/8/7.
 */

public class HeaderWrapper<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int BASE_ITEM_TYPE_HEADER = 100000;

    private SparseArrayCompat<View> mHeaderViews = new SparseArrayCompat<>();

    private RecyclerView.Adapter mInnerAdapter;
    private RecyclerView recyclerView;

    public HeaderWrapper(RecyclerView recyclerView,RecyclerView.Adapter adapter) {
        this.recyclerView = recyclerView;
        mInnerAdapter = adapter;
    }

    public void notifyChanged() {
        notifyDataSetChanged();
    }

    public void addHeaderView(View view) {
        mHeaderViews.put(mHeaderViews.size() + BASE_ITEM_TYPE_HEADER, view);
    }

    public int getHeadersCount() {

        return mHeaderViews.size();
    }

    private boolean isHeaderViewPos(int position) {
        return position < getHeadersCount();
    }

    @Override
    public int getItemViewType(int position) {

        if (isHeaderViewPos(position)){
            return mHeaderViews.keyAt(position);
        }

        return mInnerAdapter.getItemViewType(position);
    }

    private int getRealItemCount() {

        return mInnerAdapter.getItemCount();
    }

    @Override
    public int getItemCount() {

        return getHeadersCount() + getRealItemCount();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.i("onBindViewHolder",String.valueOf(position));
        if (isHeaderViewPos(position)){
            View v = holder.itemView.findViewById(R.id.scrollView);
            v.setTag(recyclerView);
            return;
        }
        mInnerAdapter.onBindViewHolder(holder,position - getHeadersCount());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderViews.get(viewType) != null){
            HeaderViewHolder viewHolder = new HeaderViewHolder(mHeaderViews.get(viewType));
            return viewHolder;
        }

        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }



    public View getHeaderView(int position){
        if (!isHeaderViewPos(position)){
            return null;
        }
        return mHeaderViews.get(getItemViewType(position));
    }




    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        public HeaderViewHolder(View itemView) {
            super(itemView);

        }
    }
}
