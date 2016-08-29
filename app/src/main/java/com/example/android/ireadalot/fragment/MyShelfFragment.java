package com.example.android.ireadalot.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.android.ireadalot.R;
import com.example.android.ireadalot.activity.BookDetailsActivity;
import com.example.android.ireadalot.adapter.BookAdapter;
import com.example.android.ireadalot.model.Book;
import com.example.android.ireadalot.utils.Constants;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyShelfFragment extends Fragment {

    private static final String LOG_TAG = "MyShelfFragment";

    private FirebaseRecyclerAdapter<Book, BookAdapter.BookViewHolder> mBookAdapter;

    public MyShelfFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_shelf, container, false);

        final DatabaseReference firebaseDBReference = FirebaseDatabase.getInstance().getReference().child("myShelfBooks");

        final Firebase myShelfListRef = new Firebase(Constants.FIREBASE_URL_MY_SHELF_LIST);
        myShelfListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(LOG_TAG, "The Data Has Changed!");
                Log.d(LOG_TAG, "There are " + dataSnapshot.getChildrenCount() + " books in the list.");

                for (DataSnapshot bookSnapshot : dataSnapshot.getChildren()) {
                    Book book = bookSnapshot.getValue(Book.class);
                    Log.d(LOG_TAG, book.getVolumeInfo().getTitle() + " - " + book.getVolumeInfo().getAuthors());
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        final RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.my_shelf_recycler_view);

          mBookAdapter = new FirebaseRecyclerAdapter<Book, BookAdapter.BookViewHolder>(
                Book.class,
                R.layout.book_list_item,
                BookAdapter.BookViewHolder.class,
                firebaseDBReference
        ) {
            @Override
            public void populateViewHolder( BookAdapter.BookViewHolder bookViewHolder, final Book book, final int position) {
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

                bookViewHolder.mBookCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d(LOG_TAG, "You Clicked on: " + mBookAdapter.getRef(position).getKey());
                        mBookAdapter.getRef(position).getKey();
                        Intent intent = new Intent(getContext(), BookDetailsActivity.class);
                        intent.putExtra(BookDetailsFragment.EXTRA_BOOK, book);
                        startActivity(intent);
                    }
                });
            }
        };

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mBookAdapter);

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
