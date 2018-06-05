package com.celerstudio.wreelysocial.views.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.celerstudio.wreelysocial.R;
import com.celerstudio.wreelysocial.models.Company;
import com.celerstudio.wreelysocial.models.Vendor;
import com.squareup.picasso.Picasso;

import java.util.List;

public class VendorAdapter extends RecyclerView.Adapter<VendorAdapter.DataObjectHolder> {
    private static String LOG_TAG = "CommonItemAdapter";
    public List<Vendor> mDataset;
    private static MyClickListener myClickListener;
    private Context context;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView name;
        ImageView logo;
        LinearLayout selector;

        public DataObjectHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            logo = (ImageView) itemView.findViewById(R.id.logo);
            selector = (LinearLayout) itemView.findViewById(R.id.selector);
            selector.setOnClickListener(this);
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

    public VendorAdapter(List<Vendor> myDataset, Context context) {
        this.context = context;
        this.mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_vendor, parent, false);
        context = parent.getContext();
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.setIsRecyclable(false);
        Vendor item = mDataset.get(position);
        holder.name.setText(item.getName());
        Picasso.with(context).load(item.getVendorLogoUrl()).into(holder.logo);
        holder.selector.setTag(item);
    }


    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    public void addItems(List<Vendor> items) {
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