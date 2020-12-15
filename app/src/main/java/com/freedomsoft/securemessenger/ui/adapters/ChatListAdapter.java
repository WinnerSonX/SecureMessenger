package com.freedomsoft.securemessenger.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.freedomsoft.securemessenger.LiveData;
import com.freedomsoft.securemessenger.R;
import com.freedomsoft.securemessenger.api.models.ChatActivity;
import com.freedomsoft.securemessenger.room.models.Chat;
import com.freedomsoft.securemessenger.security.Encryptor;

import java.util.ArrayList;
import java.util.List;


public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatHolder> {
    private final LayoutInflater layoutInflater;
    private Encryptor encryptor;
    private List<Chat> chatList = new ArrayList<>();

    public ChatListAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
        this.encryptor = Encryptor.newInstance();
    }

    public ChatListAdapter(Context context, List<Chat> chatList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.chatList = chatList;
    }

    public void setChatList(List<Chat> chatList) {
        this.chatList = chatList;
    }

    public void add(Chat chat) {
        chatList.add(chat);
        notifyItemInserted(getItemCount()-1);
    }

    @NonNull
    @Override
    public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatHolder(layoutInflater.inflate(R.layout.chat_item, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull ChatHolder holder, int position) {
        Chat chat = chatList.get(position);
        encryptor=new Encryptor();
        encryptor.setSecretKey(chat.getAesKey());
        holder.userId.setText(chat.getUserId());
        if (chat.getLastMessage() != null)
            holder.lastMessage.setText(encryptor.decrypt(chat.getLastMessage(), chat.getLasIv()));
        else holder.lastMessage.setText("");
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    class ChatHolder extends RecyclerView.ViewHolder {
        private TextView lastMessage;
        private TextView userId;
        private RelativeLayout relativeLayout;
        public ChatHolder(@NonNull View itemView) {
            super(itemView);
            lastMessage = itemView.findViewById(R.id.last_message);
            userId = itemView.findViewById(R.id.user_id);
            relativeLayout=itemView.findViewById(R.id.chat_item_root);
            relativeLayout.setOnClickListener(v -> {
                int pos=getAdapterPosition();
                if(pos==-1)return;
                LiveData.getInstance().currentChat.setValue(chatList.get(pos));
                Intent intent=new Intent(layoutInflater.getContext(), ChatActivity.class);
                layoutInflater.getContext().startActivity(intent);
            });
        }
    }
}
