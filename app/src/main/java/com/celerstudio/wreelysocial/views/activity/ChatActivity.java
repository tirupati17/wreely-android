package com.celerstudio.wreelysocial.views.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.celerstudio.wreelysocial.R;
import com.celerstudio.wreelysocial.VerticalSpaceItemDecoration;
import com.celerstudio.wreelysocial.models.Chat;
import com.celerstudio.wreelysocial.models.User;
import com.celerstudio.wreelysocial.models.Vendor;
import com.celerstudio.wreelysocial.persistence.ChatMessage;
import com.celerstudio.wreelysocial.persistence.DatabaseUtils;
import com.celerstudio.wreelysocial.persistence.WreelyDataViewModel;
import com.celerstudio.wreelysocial.util.Util;
import com.celerstudio.wreelysocial.viewModel.SkyAppDataViewModel;
import com.celerstudio.wreelysocial.views.adapter.ChatMessageAdapter;
//import com.google.firebase.database.ChildEventListener;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.Query;
//import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.xw.repo.XEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatActivity extends BaseActivity implements BaseActivity.OptionMenuListener {

    User friend;
    Vendor vendor;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.message)
    XEditText message;

    @BindView(R.id.send)
    ImageView send;
    private User user;

    private WreelyDataViewModel skyAppDataViewModel;
    private ChatMessageAdapter chatMessageAdapter;
//    private DatabaseReference databaseReference;
//    private Query getDataQuery;
//    private ValueEventListener getChatValueEventListener;
//    private ChildEventListener listenForChatChildEventListener;
    List<ChatMessage> chatMessages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        user = getApp().getPreferences().getUser();
        friend = getIntent().getParcelableExtra("friend");
        if (getIntent().hasExtra("vendor"))
            vendor = getIntent().getParcelableExtra("vendor");

        if (friend.getName().equalsIgnoreCase("group chat")) {
//            databaseReference = getApp().getFirebaseDBRef("groupChat/" + chatRoomId());
        }

        chatMessageAdapter = new ChatMessageAdapter(chatMessages, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(16, true));
        recyclerView.setAdapter(chatMessageAdapter);

//        fetchData();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        enableDisableSend(false);

        message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                enableDisableSend(Util.textIsEmpty(charSequence.toString()) ? false : true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

//        message.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    if (recyclerView.getAdapter().getItemCount() > 0)
//                        recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount());
//                }
//            }
//        });

        if (Build.VERSION.SDK_INT >= 11) {
            recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v,
                                           int left, int top, int right, int bottom,
                                           int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    if (bottom < oldBottom) {
                        recyclerView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (recyclerView.getAdapter().getItemCount() > 0) {
                                    recyclerView.smoothScrollToPosition(
                                            recyclerView.getAdapter().getItemCount() - 1);
                                }
                            }
                        }, 100);
                    }
                }
            });
        }
        toolbar.setTitle(friend.getName());
        listenForIncomingMessage();
        if (!friend.getName().equalsIgnoreCase("group chat")) {
            setOptionMenu(R.menu.chat, this);
        } else {
            toolbar.setTitle("Group Conversations");
            setOptionMenu(R.menu.group_chat, this);
        }

    }

    public void enableDisableSend(boolean enable) {
        send.setAlpha(enable ? 1f : 0.3f);
        send.setEnabled(enable);
        send.setClickable(enable);
    }

    @OnClick(R.id.send)
    void onSend() {
        if (!Util.textIsEmpty(message.getText().toString())) {
//            DatabaseReference db = databaseReference.push();
            Chat chat = new Chat();
            chat.setMessage(message.getText().toString());
            chat.setSenderId(user.getAccessToken());
            chat.setReceiverId("GroupChat" + friend.getAccessToken());
            chat.setUserName(user.getName());
            chat.setTimestamp(System.currentTimeMillis());
            chat.setRoomId(chatRoomId());
            //DatabaseUtils.saveChatMessage(this, Chat.copy(chat));
            message.setText("");
//            db.setValue(chat);
        }
    }

    private void fetchData() {
//        getChatValueEventListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.getValue() != null)
//                    collectChats((Map<String, Object>) dataSnapshot.getValue());
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.d("databaseError", databaseError.getMessage());
//            }
//        };
//
////        chatHomeActivity.showDialog("Friends", "Wait while we get your friends");
//        getDataQuery = databaseReference.limitToFirst(100);
//        getDataQuery.addListenerForSingleValueEvent(getChatValueEventListener);
    }

    private void collectChats(Map<String, Object> values) {
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            Map mapChat = (Map) entry.getValue();
            Chat chat = Chat.map(user.getAccessToken(), mapChat);
//            chatMessages.add(Chat.cloneToChatMessage(chat));
        }
//        chatMessageAdapter.addItems(chatMessages);
//        recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount());
    }

    private void listenForIncomingMessage() {
//        listenForChatChildEventListener = new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                if (dataSnapshot.getValue() != null) {
//                    HashMap mapMessage = (HashMap) dataSnapshot.getValue();
//                    Chat newMessage = Chat.map(user.getAccessToken(), mapMessage);
//                    ChatMessage chatMessage = Chat.cloneToChatMessage(newMessage);
//                    Log.d("chatMessage", new Gson().toJson(chatMessage));
//                    chatMessageAdapter.add(chatMessage);
//                    recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount());
//                }
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        };
//        databaseReference.addChildEventListener(listenForChatChildEventListener);
    }

    private String chatRoomId() {
        String chatRoomId = String.valueOf(friend.getAccessToken().hashCode() + user.getAccessToken().hashCode());
        if (friend.getName().equalsIgnoreCase("group chat")) {
            chatRoomId = friend.getId();
        }
        return chatRoomId;
    }

    @Override
    public void onMenuClicked(MenuItem item) {
        if (!friend.getName().equalsIgnoreCase("group chat")) {
            showDialog();
        } else {
            Intent intent = new Intent(this, GroupDetailActivity.class);
            intent.putExtra("vendor", vendor);
            startActivity(intent);
        }
    }

    private void showDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.layout_chat_user_info, null);
        TextView title = alertLayout.findViewById(R.id.title);
        TextView instituteName = alertLayout.findViewById(R.id.institute_name);
        TextView state = alertLayout.findViewById(R.id.state);
        title.setText(friend.getName());
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(true);
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (getDataQuery != null && getChatValueEventListener != null) {
//            getDataQuery.removeEventListener(getChatValueEventListener);
//        }
//
//        if (databaseReference != null && listenForChatChildEventListener != null) {
//            databaseReference.removeEventListener(listenForChatChildEventListener);
//        }
    }
}
