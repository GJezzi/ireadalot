package com.example.android.ireadalot.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.ireadalot.R;
import com.example.android.ireadalot.adapter.BookAdapter;
import com.example.android.ireadalot.model.Book;

import java.util.ArrayList;


public class MyShelfFragment extends Fragment {

    private static final String LOG_TAG = MyShelfFragment.class.getSimpleName();

    private final static int BOOK_DETAILS_ACTIVITY_REQUEST = 1;

    private ArrayList<Book> mMyShelfBookList = new ArrayList<>();
    private BookAdapter mBookAdapter;
    private RecyclerView mRecyclerView;
    private Book mBook;

    private Context mContext;

    public MyShelfFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_shelf, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_shelf_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //mRecyclerView.setHasFixedSize(true);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == BOOK_DETAILS_ACTIVITY_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                mBookAdapter = new BookAdapter(mContext, mMyShelfBookList);
                mRecyclerView.setAdapter(mBookAdapter);
                mBookAdapter.notifyDataSetChanged();
            }
        }
    }

    public static MyShelfFragment newInstance(){
        MyShelfFragment myShelfFragment = new MyShelfFragment();
        Bundle args = new Bundle();
        myShelfFragment.setArguments(args);
        return myShelfFragment;

    }
}
