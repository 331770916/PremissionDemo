package com.lib.weight.treelist;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lib.weight.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TreeListAdapter extends BaseTreeRVAdapter<TreeItem> {

    private TreeOnItemClickListener mListener;

    public void setOnItemClickListenr(TreeOnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_tree_list, parent, false);
        RecyclerView.ViewHolder vh = new TreeViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(int type, final TreeItem tree, RecyclerView.ViewHolder holder) {
        final TreeViewHolder treeViewHolder = (TreeViewHolder) holder;
        TreeEntity treeEntity = (TreeEntity)tree;
        treeViewHolder.tv_tree_title.setText(treeEntity.getTitle());
        treeViewHolder.iv_tree.setVisibility(View.VISIBLE);
        treeViewHolder.iv_tree.setImageResource(tree.isExpand() ? R.mipmap.icon_down : R.mipmap.icon_up);
        treeViewHolder.rl_tree_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (null != tree.getChilds()) {
                    tree.setOpen(!tree.isExpand());
                    notifyDataSetChanged();
                } else {
                    if (mListener != null) {
                        mListener.onItemClick();
                    }
                }

            }
        });

        if (null == treeEntity.getChilds()) {
            treeViewHolder.iv_tree.setVisibility(View.GONE);
        }

    }

    class TreeViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout rl_tree_group;
        private TextView tv_tree_title;
        private ImageView iv_tree;

        public TreeViewHolder(@NonNull View itemView) {
            super(itemView);
            rl_tree_group = (RelativeLayout)itemView.findViewById(R.id.rl_tree_group);
            tv_tree_title = (TextView)itemView.findViewById(R.id.tv_tree_title);
            iv_tree = (ImageView)itemView.findViewById(R.id.iv_tree);
        }


    }
}
