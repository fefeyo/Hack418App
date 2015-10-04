package com.team_a.hack418app;

import android.content.Context;
import android.graphics.Typeface;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.mlkcca.client.DataElement;
import com.mlkcca.client.DataElementValue;
import com.mlkcca.client.DataStore;
import com.mlkcca.client.DataStoreEventListener;
import com.mlkcca.client.MilkCocoa;
import com.mlkcca.client.Streaming;
import com.mlkcca.client.StreamingListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements ValueEventListener {

    private Firebase mFirebase;
    private ArrayList<ThreadItem> arr;
    private ListView mListView;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);
        mFirebase = new Firebase("https://hack418.firebaseio.com/");
        mFirebase.addValueEventListener(this);
        mListView = (ListView)findViewById(R.id.listview);
        title = (TextView)findViewById(R.id.title);
        title.setTypeface(Typeface.createFromAsset(getAssets(), "honoka.ttf"));
    }

    public void push(View v) {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:SS", Locale.JAPAN);
        final Firebase fb = mFirebase.child(sdf.format(new Date()));
        fb.child("name").setValue("だれが");
        fb.child("what").setValue("なにを");
        fb.child("benefit").setValue("ほうしゅうは");
        fb.child("limit").setValue("いつまでに");
        fb.child("where").setValue("どこで");
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        arr = new ArrayList<>();
        if (dataSnapshot.getValue().getClass().equals(HashMap.class)) {
            final HashMap<String, HashMap<String, String>> map = ((HashMap) dataSnapshot.getValue());
            for (Map.Entry<String, HashMap<String, String>> e : map.entrySet()) {
                final HashMap<String, String> thread_value = (HashMap) e.getValue();
                final ThreadItem item = new ThreadItem();
                item.setName(thread_value.get("name"));
                item.setWhat(thread_value.get("what"));
                item.setBenefit(thread_value.get("benefit"));
                item.setLimit(thread_value.get("limit"));
                item.setWhere(thread_value.get("where"));
                arr.add(item);
            }
        }
        setAdapter();
    }

    private void setAdapter(){
        mListView.setAdapter(new ThreadAdapter(
                getApplicationContext(),
                0,
                arr
        ));
    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {

    }

    private class ThreadAdapter extends ArrayAdapter {

        public ThreadAdapter(Context context, int resource, final ArrayList<ThreadItem> arr) {
            super(context, resource, arr);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (null == convertView)
                convertView = getLayoutInflater().inflate(R.layout.item_row, null);
            final ThreadItem item = (ThreadItem) getItem(position);
            final ImageView right = (ImageView)convertView.findViewById(R.id.right_pin);
            final ImageView left = (ImageView)convertView.findViewById(R.id.left_pin);
            if(0 == position % 2){
                right.setImageResource(R.drawable.aka);
                left.setImageResource(R.drawable.aka);
            }else{
                right.setImageResource(R.drawable.ao);
                left.setImageResource(R.drawable.ao);
            }
            final TextView name = (TextView) convertView.findViewById(R.id.name);
            final TextView where = (TextView) convertView.findViewById(R.id.where);
            final TextView what = (TextView) convertView.findViewById(R.id.what);
            name.setTypeface(Typeface.createFromAsset(getAssets(), "honoka.ttf"));
            where.setTypeface(Typeface.createFromAsset(getAssets(), "honoka.ttf"));
            what.setTypeface(Typeface.createFromAsset(getAssets(), "honoka.ttf"));
            name.setText(item.getName());
            where.setText(item.getWhere());
            what.setText(item.getWhat());

            return convertView;
        }
    }

}