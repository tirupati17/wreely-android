package com.celerstudio.wreelysocial.views.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
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
import com.celerstudio.wreelysocial.models.MeetingRoom;
import com.celerstudio.wreelysocial.models.MeetingRoomSlot;
import com.celerstudio.wreelysocial.models.User;
import com.celerstudio.wreelysocial.util.Util;

import java.util.List;

public class MeetingRoomSlotAdapter extends RecyclerView.Adapter<MeetingRoomSlotAdapter.DataObjectHolder> {
    private static String LOG_TAG = "CommonItemAdapter";
    public List<MeetingRoomSlot> mDataset;
    private User user;
    private static MyClickListener myClickListener;
    private Context context;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView name;
        ImageView logo;
        LinearLayout selector;
        CheckBox check;

        public DataObjectHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
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

    public MeetingRoomSlotAdapter(List<MeetingRoomSlot> myDataset, Context context, User user) {
        this.context = context;
        this.mDataset = myDataset;
        this.user = user;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_meeting_room_slot, parent, false);
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
        holder.name.setText(startTime + " - " + endTime);

        if (item.isExpired()) {
            holder.check.setEnabled(false);
            holder.selector.setAlpha(0.3f);
            holder.selector.setOnClickListener(null);
        } else {
            if (item.isAvailable()) {
                holder.check.setChecked(false);
                holder.check.setEnabled(true);
            } else {
                holder.check.setChecked(true);
                if (Integer.parseInt(user.getId()) == item.getBookedByMemberId())
                    holder.check.setEnabled(true);
                else
                    holder.check.setEnabled(false);
            }
        }

//        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
// generate random color
//        int color1 = generator.getRandomColor();
// generate color based on a key (same key returns the same color), useful for list/grid views
//        int color2 = generator.getColor(item.getContactPersonEmailId());
//        TextDrawable drawable = TextDrawable.builder()
//                .buildRound(String.valueOf(position + 1), color1);
//        holder.logo.setImageDrawable(drawable);
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