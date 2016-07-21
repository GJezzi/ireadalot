package com.example.android.ireadalot.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

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

    public BookFragment() { setHasOptionsMenu(true); }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        View rootView = inflater.inflate(R.layout.fragment_books, container, false);

        final RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.books_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<BookResponse> call = apiInterface.getBooks(USER_SEARCH);
        call.enqueue(new Callback<BookResponse>() {
            @Override
            public void onResponse(Call<BookResponse> call, Response<BookResponse> response) {
                int okStatusCode = response.code();
                if(response.isSuccessful()) {
                    ArrayList<Book> books = response.body().getItems();
                    BookAdapter adapter = new BookAdapter(getContext(), books);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater){
        menuInflater.inflate(R.menu.action_search_book, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        int id = menuItem.getItemId();
        if (id == R.id.action_search) {
            createSearchDialog();
            return true;
        }

        return super.onOptionsItemSelected(menuItem);
    }

    private void createSearchDialog() {
        final AlertDialog.Builder searchDialog = new AlertDialog.Builder(getContext());
        searchDialog.setTitle(R.string.search_dialog_title);

        final EditText bookInput = new EditText(getContext());
        searchDialog.setView(bookInput);

        searchDialog.setPositiveButton("Search", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String userResult = bookInput.getText().toString();
                Toast.makeText(getContext(), "Searching Books", Toast.LENGTH_SHORT).show();
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
}
