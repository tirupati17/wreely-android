package com.celerstudio.wreelysocial.views.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.celerstudio.wreelysocial.R;
import com.celerstudio.wreelysocial.models.MeetingRoomSlot;
import com.celerstudio.wreelysocial.models.User;
import com.celerstudio.wreelysocial.util.Util;

import java.util.List;

public class MeetingRoomSlotHistoryAdapter extends RecyclerView.Adapter<MeetingRoomSlotHistoryAdapter.DataObjectHolder> {
    private static String LOG_TAG = "CommonItemAdapter";
    public List<MeetingRoomSlot> mDataset;
    private static MyClickListener myClickListener;
    private Context context;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView name, time;
        ImageView logo;
        LinearLayout selector;
        CheckBox check;

        public DataObjectHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            time = (TextView) itemView.findViewById(R.id.time);
            logo = (ImageView) itemView.findViewById(R.id.logo);
            selector = (LinearLayout) itemView.findViewById(R.id.selector);
            check = (CheckBox) itemView.findViewById(R.id.check);
            selector.setOnClickListener(this);
//            check.setOnClickListener(this);
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

    public MeetingRoomSlotHistoryAdapter(List<MeetingRoomSlot> myDataset, Context context) {
        this.context = context;
        this.mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_meeting_room_slot_history, parent, false);
        context = parent.getContext();
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.setIsRecyclable(false);
        MeetingRoomSlot item = mDataset.get(position);
        String startTime = Util.getFriendlyTime(item.getStartTime().split("\\s")[1]);
        String endTime = Util.getFriendlyTime(item.getEndTime().split("\\s")[1]);
        holder.name.setText(item.getName());
        holder.time.setText(startTime + " - " + endTime);

        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
// generate random color
        int color1 = generator.getRandomColor();
// generate color based on a key (same key returns the same color), useful for list/grid views
//        int color2 = generator.getColor(item.getContactPersonEmailId());
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(String.valueOf(position + 1), color1);
        holder.logo.setImageDrawable(drawable);
        holder.selector.setTag(item);
    }


    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    public void addItems(List<MeetingRoomSlot> items) {
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