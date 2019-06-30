package com.tiva11.gettingstartedwithandroidwear;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends WearableActivity {
//    private TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        mTextView = (TextView) findViewById(R.id.text);
//        createPeopleCounterDemo();
        createBooksRecyclerView();
//        createFriendsListViewApp();
        // Enables Always-on
        setAmbientEnabled();
    }
    public void toastScreenShape(View v) {
        Toast.makeText(this.getApplicationContext(), R.string.hello_world,Toast.LENGTH_SHORT).show();
        Log.i("MIKI",getResources().getString(
                getResources().getConfiguration().isScreenRound() ? R.string.roundScreen : R.string.squareScreen));
    }
    //
    //People Counter Functions
    //
    private void createPeopleCounterDemo(){
        setContentView(R.layout.peoplecounter);
        mTextViewCounter = (TextView) findViewById(R.id.textViewCounter);
        updateCounterOnScreen();
    }
    private int mCounter = 0;
    private TextView mTextViewCounter;
    public void plusOne(View v) {
        mCounter++;
        updateCounterOnScreen();
    }
    public void resetCounter(View v) {
        mCounter = 0;
        updateCounterOnScreen();
    }
    void updateCounterOnScreen(){
        mTextViewCounter.setText(Integer.toString(mCounter));
    }
    //
    //Books Recycler View Functions
    //
    private void createBooksRecyclerView(){
        setContentView(R.layout.recyclerlist);
        RecyclerView rv = (RecyclerView)findViewById(R.id.rvList);
        BooksRVAdapter booksAdapter = new BooksRVAdapter();
//        RecyclerView.LayoutManager lm = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(booksAdapter);
        rv.setHasFixedSize(true);
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new BooksRVAdapter.SwipeToDeleteBook(booksAdapter));
        itemTouchHelper.attachToRecyclerView(rv);
    }
    private void createFriendsListViewApp(){
        setContentView(R.layout.wearlist);
        ListView lv = (ListView)findViewById(R.id.listView);
        String[] friends = {"Zsuzsa","Erika","Attila","Clive","Xavier","Mihaela","Karcsi"};
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,friends);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(view.getContext(),friends[position],Toast.LENGTH_SHORT).show();
            }
        });
    }
}
