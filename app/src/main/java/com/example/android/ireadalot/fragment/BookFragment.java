package com.example.android.ireadalot.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.ireadalot.R;
import com.example.android.ireadalot.adapter.BookAdapter;
import com.example.android.ireadalot.model.Book;
import com.example.android.ireadalot.model.BookResponse;
import com.example.android.ireadalot.rest.ApiClient;
import com.example.android.ireadalot.rest.ApiInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BookFragment extends Fragment {

    private static final String LOG_TAG = BookFragment.class.getSimpleName();
    private static final String USER_SEARCH = "Tolkien";

    private RecyclerView mRecyclerView;

    public BookFragment() { setHasOptionsMenu(true); }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        View rootView = inflater.inflate(R.layout.fragment_books, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.books_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<BookResponse> call = apiInterface.getBooks(USER_SEARCH);
        call.enqueue(new Callback<BookResponse>() {
            @Override
            public void onResponse(Call<BookResponse> call, Response<BookResponse> response) {
                int okStatusCode = response.code();
                if(response.isSuccessful()) {
                    ArrayList<Book> books = response.body().getItems();
                    BookAdapter bookAdapter = new BookAdapter(getContext(), books);
                    mRecyclerView.setAdapter(bookAdapter);
                    bookAdapter.notifyDataSetChanged();

                }
                else {
                    Log.e(LOG_TAG, "Error");
                }
            }

            @Override
            public void onFailure(Call<BookResponse> call, Throwable t) {
                Log.e(LOG_TAG, t.getMessage());
            }
        });
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

    public static BookFragment newInstance() {
        BookFragment bookFragment = new BookFragment();
        Bundle args = new Bundle();
        bookFragment.setArguments(args);
        return bookFragment;

    }
}
