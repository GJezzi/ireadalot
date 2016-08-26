package com.example.android.ireadalot.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.android.ireadalot.R;
import com.example.android.ireadalot.adapter.BookAdapter;
import com.example.android.ireadalot.model.Book;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyShelfFragment extends Fragment {

    private static final String LOG_TAG = "MyShelfFragment";

    private DatabaseReference mFirebaseDBReference;

    public MyShelfFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_shelf, container, false);

        //Firebase myShelfListRef = new Firebase(Constants.FIREBASE_URL_MY_SHELF_LIST);
        mFirebaseDBReference = FirebaseDatabase.getInstance().getReference().child("myShelfBooks");


        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.my_shelf_recycler_view);


        FirebaseRecyclerAdapter<Book, BookAdapter.BookViewHolder> bookAdapter = new FirebaseRecyclerAdapter<Book, BookAdapter.BookViewHolder>(
                Book.class,
                R.layout.book_list_item,
                BookAdapter.BookViewHolder.class,
                mFirebaseDBReference
        ) {
            @Override
            public void populateViewHolder(final BookAdapter.BookViewHolder bookViewHolder, final Book book, int position) {
                StringBuilder builder = new StringBuilder();

                bookViewHolder.mBookTitle.setText(book.getVolumeInfo().getTitle());
                bookViewHolder.mBookDesc.setText(book.getVolumeInfo().getDescription());

                for (String string : book.getVolumeInfo().getAuthors()) {
                    if (builder.length() > 0) {
                        builder.append(", ");
                    }
                    builder.append(string);
                }

                bookViewHolder.mAuthorName.setText(builder.toString());

                Glide.with(getContext())
                        .load(book.getVolumeInfo().getImageLinks().getThumbnail())
                        .into(bookViewHolder.mBookImage);
            }
        };

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(bookAdapter);

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

    public static MyShelfFragment newInstance(){
        MyShelfFragment myShelfFragment = new MyShelfFragment();
        Bundle args = new Bundle();
        myShelfFragment.setArguments(args);
        return myShelfFragment;

    }
}
