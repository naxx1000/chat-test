package com.chat.cociocompany.firebasechattest;


import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<ChatDataProvider> mDataset;

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView mTextName,mTextMessage;
        public LinearLayout mLinearLayout;
        public ViewHolder(View v){
            super(v);
            mTextName = (TextView)v.findViewById(R.id.textViewChatName);
            mTextMessage = (TextView)v.findViewById(R.id.textViewChatMessage);
            mLinearLayout = (LinearLayout)v.findViewById(R.id.linearLayoutChatList);
        }
    }

    public MyAdapter(List<ChatDataProvider> myDataSet){
        this.mDataset = myDataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list_layout,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        ChatDataProvider dataProvider = mDataset.get(position);

        holder.mTextMessage.setText(dataProvider.getMessage());
        //color change when item is the user
        if(dataProvider.getName().equals(FriendChat.name)){
            holder.mLinearLayout.setGravity(Gravity.END);
            holder.mTextMessage.setBackgroundResource(R.drawable.chat_bubble_you);
        }else{
            holder.mLinearLayout.setGravity(Gravity.START);
            holder.mTextMessage.setBackgroundResource(R.drawable.chat_bubble_other);
        }
        holder.mTextName.setText(dataProvider.getName());
    }

    @Override
    public int getItemCount(){
        return mDataset.size();
    }

}
