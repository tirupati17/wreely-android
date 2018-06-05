package com.celerstudio.wreelysocial.views.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.celerstudio.wreelysocial.R;
import com.celerstudio.wreelysocial.persistence.ChatMessage;

import java.util.List;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.DataObjectHolder> {
    private static String LOG_TAG = "CommonItemAdapter";
    public List<ChatMessage> mDataset;
    private static MyClickListener myClickListener;
    private Context context;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView recvd, sent, name;
        RelativeLayout receivedCont, sentCont;
        LinearLayout selector;

        public DataObjectHolder(View itemView) {
            super(itemView);
            recvd = (TextView) itemView.findViewById(R.id.recvd);
            sent = (TextView) itemView.findViewById(R.id.sent);
            name = (TextView) itemView.findViewById(R.id.name);
            receivedCont = (RelativeLayout) itemView.findViewById(R.id.received_cont);
            sentCont = (RelativeLayout) itemView.findViewById(R.id.sent_cont);
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

    public ChatMessageAdapter(List<ChatMessage> myDataset, Context context) {
        this.context = context;
        this.mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message, parent, false);
        context = parent.getContext();
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.setIsRecyclable(false);
        ChatMessage item = mDataset.get(position);
        holder.receivedCont.setVisibility(View.GONE);
        holder.sentCont.setVisibility(View.GONE);
        holder.name.setVisibility(View.GONE);
        if (item.isIncoming()) {
            if (item.getReceiverId().toLowerCase().contains("groupchat")) {
                holder.name.setVisibility(View.VISIBLE);
                holder.name.setText(item.getUserName());
            }
            holder.receivedCont.setVisibility(View.VISIBLE);
            holder.recvd.setText(item.getMessage());
        } else {
            holder.sentCont.setVisibility(View.VISIBLE);
            holder.sent.setText(item.getMessage());
        }
        holder.selector.setTag(item);
    }


    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    public void addItems(List<ChatMessage> items) {
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