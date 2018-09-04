package com.celerstudio.wreelysocial.views.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.celerstudio.wreelysocial.R;
import com.celerstudio.wreelysocial.databinding.ItemNearbyWorkspaceBinding;
import com.celerstudio.wreelysocial.models.NearbyWorkspace;
import com.celerstudio.wreelysocial.util.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class NearbyWorkspacesAdapter extends RecyclerView.Adapter<NearbyWorkspacesAdapter.DataObjectHolder> {
    private static String LOG_TAG = "CommonItemAdapter";
    public List<NearbyWorkspace> mDataset;
    private static MyClickListener myClickListener;
    private Context context;

    public class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {

        ItemNearbyWorkspaceBinding binding;

        public DataObjectHolder(ItemNearbyWorkspaceBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.selector.setOnClickListener(this);
            this.binding.direction.setOnClickListener(this);
        }

        public void bind(NearbyWorkspace info) {
            this.binding.setInfo(info);
            this.binding.executePendingBindings();
        }

        public ItemNearbyWorkspaceBinding getBinding() {
            return binding;
        }

        @Override
        public void onClick(View v) {
            if (myClickListener != null)
                myClickListener.onItemClick(getPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public NearbyWorkspacesAdapter(List<NearbyWorkspace> myDataset, Context context) {
        this.context = context;
        this.mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemNearbyWorkspaceBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_nearby_workspace, parent, false);
        context = parent.getContext();
        return new DataObjectHolder(binding);
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.setIsRecyclable(false);
        NearbyWorkspace item = mDataset.get(position);

        if (item.getId().equalsIgnoreCase("-420") || item.getId().equalsIgnoreCase("-421")) {
            holder.binding.monthPrice.setVisibility(View.GONE);
            holder.binding.direction.setVisibility(View.GONE);
        } else {
            Picasso.with(context).load(item.getImage()).into(holder.binding.pic);
            float mp = Util.textIsEmpty(item.getMonthPrice()) ? 0 : Float.parseFloat(item.getMonthPrice());
            if (mp <= 0) {
                holder.binding.monthPrice.setVisibility(View.GONE);
            } else {
                holder.binding.monthPrice.setText(item.getCurrency() + " " + item.getMonthPrice() + "/Month");
            }
        }

        holder.bind(item);
    }


    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    public void addItems(List<NearbyWorkspace> items) {
        this.mDataset = items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }

}