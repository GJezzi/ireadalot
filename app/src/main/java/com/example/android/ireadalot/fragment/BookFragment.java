package com.example.android.ireadalot.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.ireadalot.R;
import com.example.android.ireadalot.activity.BookDetailsActivity;
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

    private Context mContext;
    private Book mBook;
    private ArrayList<Book> mBooksList;
    private RecyclerView mRecyclerView;
    private BookAdapter.OnBookClickListener mBookClickListener;
    private FloatingActionButton mFab;
    private ApiInterface mApiInterface;

    public BookFragment() { setHasOptionsMenu(true); }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        View rootView = inflater.inflate(R.layout.fragment_books, container, false);

        mContext = getContext();
        mFab = (FloatingActionButton) rootView.findViewById(R.id.fab_book_search);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.books_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mApiInterface = ApiClient.getClient().create(ApiInterface.class);

        searchBook();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        searchBook();
    }

    @Override
    public void onPause() {
        super.onPause();
        searchBook();
    }

    public static BookFragment newInstance() {
        BookFragment bookFragment = new BookFragment();
        Bundle args = new Bundle();
        bookFragment.setArguments(args);
        return bookFragment;

    }

    private void createSearchDialog() {
        final AlertDialog.Builder searchDialog = new AlertDialog.Builder(getActivity());
        searchDialog.setTitle(R.string.search_dialog_title);

        final EditText bookInput = new EditText(getActivity());
        searchDialog.setView(bookInput);

        searchDialog.setPositiveButton("Search", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String userResult = bookInput.getText().toString();
                Toast.makeText(getActivity(), "Searching: " + userResult, Toast.LENGTH_SHORT).show();

                Call<BookResponse> call = mApiInterface.getBooks(userResult);
                call.enqueue(new Callback<BookResponse>() {
                    @Override
                    public void onResponse(Call<BookResponse> call, Response<BookResponse> response) {
                        int okStatusCode = response.code();
                        if(response.isSuccessful()) {
                            ArrayList<Book> books = response.body().getItems();

                            BookAdapter bookAdapter = new BookAdapter(getContext(), books, new BookAdapter.OnBookClickListener() {
                                @Override
                                public void onBookClicked(Book book) {
                                    Toast.makeText(mContext, "Book Id: " + book.getId(), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getContext(), BookDetailsActivity.class);
                                    intent.putExtra(BookDetailsFragment.EXTRA_BOOK, book);
                                    startActivityForResult(intent, BookDetailsActivity.BOOK_DETAILS_REQUEST_CODE);
                                }
                            });
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
                dialogInterface.dismiss();
            }
        });
        searchDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        searchDialog.create();
        searchDialog.show();
    }

    public void searchBook() {
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createSearchDialog();
            }
        });
    }
}
