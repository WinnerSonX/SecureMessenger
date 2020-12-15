package com.freedomsoft.securemessenger.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.freedomsoft.securemessenger.LiveData;
import com.freedomsoft.securemessenger.R;
import com.freedomsoft.securemessenger.api.models.Message;
import com.freedomsoft.securemessenger.room.models.User;
import com.freedomsoft.securemessenger.security.Encryptor;

import java.util.ArrayList;
import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageHolder> {
    private List<Message> messageList = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private User currentUser = LiveData.getInstance().userMutableLiveData.getValue();
    private Encryptor encryptor;

    public MessagesAdapter(Context context, Encryptor encryptor) {
        this.layoutInflater = LayoutInflater.from(context);
        this.encryptor = encryptor;
//        this.secretKey = new SecretKeySpec(chat.getAesKey(), "AES");
//        encryptor = Encryptor.newInstance(chat.getAesKey());
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }

    public void notifySent(Message message) {
        messageList.add(message);
        notifyItemInserted(getItemCount() - 1);
    }

    public void notifyIncome(List<Message> messages) {

        int lastPos = getItemCount() - 1;
        messageList.addAll(messages);
        notifyItemRangeInserted(lastPos+1, getItemCount());
    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.message_item, parent, false);
        return new MessageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, int position) {
        Message message = messageList.get(position);
        holder.messageText.setText(encryptor.decrypt(message.getMessage(), message.getIv()));
        if (message.getSenderId().equals(currentUser.getId())) {
            holder.messageRound.setBackgroundResource(R.drawable.round_message_my);
            holder.messageRoot.setGravity(GravityCompat.END);
            holder.messageSubRoot.setGravity(GravityCompat.END);
            holder.messageRound.setGravity(GravityCompat.END);
        } else {
            holder.messageRound.setBackgroundResource(R.drawable.round_message);
            holder.messageRoot.setGravity(GravityCompat.START);
            holder.messageSubRoot.setGravity(GravityCompat.START);
            holder.messageRound.setGravity(GravityCompat.START);
        }

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    class MessageHolder extends RecyclerView.ViewHolder {
        private LinearLayout messageRoot;
        private LinearLayout messageSubRoot;
        private RelativeLayout messageRound;
        private TextView messageText;

        public MessageHolder(@NonNull View itemView) {
            super(itemView);
            messageRoot = itemView.findViewById(R.id.message_root_layout);
            messageSubRoot = itemView.findViewById(R.id.message_sub_root_layout);
            messageRound = itemView.findViewById(R.id.message_round_layout);
            messageText = itemView.findViewById(R.id.message_text);
        }
    }
}
