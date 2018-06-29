package com.celerstudio.wreelysocial.views.adapter;

import android.content.Context;
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
import com.celerstudio.wreelysocial.models.Company;
import com.celerstudio.wreelysocial.models.Day;

import java.util.List;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.DataObjectHolder> {
    private static String LOG_TAG = "CommonItemAdapter";
    public List<Day> mDataset;
    private static MyClickListener myClickListener;
    private Context context;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView day, dayOfMonth, month;
        View underline;
        LinearLayout selector;

        public DataObjectHolder(View itemView) {
            super(itemView);
            day = (TextView) itemView.findViewById(R.id.day);
            dayOfMonth = (TextView) itemView.findViewById(R.id.day_of_month);
            month = (TextView) itemView.findViewById(R.id.month);
            underline = (View) itemView.findViewById(R.id.underline);
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

    public DayAdapter(List<Day> myDataset, Context context) {
        this.context = context;
        this.mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_day, parent, false);
        context = parent.getContext();
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.setIsRecyclable(false);
        Day item = mDataset.get(position);
        holder.dayOfMonth.setText(String.valueOf(item.getDayOfMonth()));
        holder.day.setText(item.getDay());
        holder.month.setText(item.getMonth());
        if (item.isChecked()) {
            holder.selector.setSelected(true);
            holder.underline.setVisibility(View.VISIBLE);
        } else {
            holder.selector.setSelected(false);
            holder.underline.setVisibility(View.INVISIBLE);
        }
        holder.selector.setSelected(item.isChecked());
        holder.selector.setTag(item);
    }


    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    public void addItems(List<Day> items) {
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