package com.chat.cociocompany.firebasechattest;

import android.content.DialogInterface;
import android.content.Intent;
import android.preference.DialogPreference;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.client.realtime.util.StringListReader;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private EditText name;

    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_friends = new ArrayList<>();

    //reference to database object
    private Firebase rootRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Firebase.setAndroidContext(this);

        rootRef = new Firebase("https://chat-test-cdadd.firebaseio.com/");

        name = (EditText)findViewById(R.id.editTextName);
        listView = (ListView)findViewById(R.id.listViewRooms);

        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list_of_friends);

        //sets the room list adapter
        listView.setAdapter(arrayAdapter);

        GetFirebaseFriendData(rootRef);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String n = name.getText().toString();
                if(!n.equals("")) {
                    Intent intentFriend = new Intent(MainActivity.this, FriendChat.class);
                    String friend = (listView.getItemAtPosition(position).toString());
                    intentFriend.putExtra("FRIEND", friend);
                    intentFriend.putExtra("NAME", n);
                    startActivity(intentFriend);
                }else{
                    Toast.makeText(MainActivity.this,"Please enter your name",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void GetFirebaseFriendData(Firebase rootRef){
        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Set<String> set = new HashSet<String>();
                Iterator i = dataSnapshot.getChildren().iterator();

                while (i.hasNext()){
                    set.add(((DataSnapshot)i.next()).getKey());
                }
                list_of_friends.clear();
                list_of_friends.addAll(set);
                //notifies to change data on listview
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }
}
