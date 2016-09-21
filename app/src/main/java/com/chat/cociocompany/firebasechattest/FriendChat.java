package com.chat.cociocompany.firebasechattest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FriendChat extends AppCompatActivity {

    public List<String> arrayMessage;
    public List<String> arrayName;

    public static String name;
    private EditText editTextMsg;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private List<ChatDataProvider> myDataSet = new ArrayList<>();
    //reference to database object
    private Firebase rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_chat);
        Intent intentGet = getIntent();
        name = intentGet.getStringExtra("NAME");
        String friend = intentGet.getStringExtra("FRIEND");
        editTextMsg = (EditText)findViewById(R.id.editTextMessage);

        setTitle(friend);

        arrayMessage = new ArrayList<>();
        arrayName = new ArrayList<>();

        rootRef = new Firebase("https://chat-test-cdadd.firebaseio.com/"+friend);

        mRecyclerView = (RecyclerView)findViewById(R.id.listViewChat);
        assert mRecyclerView != null;
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new MyAdapter(myDataSet);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

        getFirebaseData(rootRef);
    }

    public void sendMessage(View view){
        try{
            String message = editTextMsg.getText().toString();
            if(!message.equals("")){
                HSPost post = new HSPost(message, name);
                rootRef.push().setValue(post);
                editTextMsg.setText("");
            }else{
                Toast.makeText(FriendChat.this, "Please write a message", Toast.LENGTH_SHORT).show();
            }
        }catch (NumberFormatException e){
            Toast.makeText(FriendChat.this, "Please write a message", Toast.LENGTH_SHORT).show();
        }
    }

    public void getFirebaseData(Firebase rootRef){
        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayMessage.clear();
                arrayName.clear();
                myDataSet.clear();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    HSPost post = postSnapshot.getValue(HSPost.class);
                    ChatDataProvider dataProvider = new ChatDataProvider(post.getMessage(), post.getName());
                    myDataSet.add(dataProvider);
                    arrayName.add(post.getName());
                    arrayMessage.add(post.getMessage());
                }
                mAdapter.notifyDataSetChanged();
                mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount() - 1);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: "+ firebaseError.getMessage()+"\n"+firebaseError.getDetails());
            }
        });
    }

    public static class HSPost{
        private String message;
        private String name;
        public HSPost(){

        }
        public HSPost(String message, String name){
            this.message = message;
            this.name = name;
        }
        //getters
        public String getMessage(){
            return message;
        }
        public String getName(){
            return name;
        }
    }

}
