package com.example.android.ireadalot.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.ireadalot.R;
import com.example.android.ireadalot.activity.BookDetailsActivity;
import com.example.android.ireadalot.model.Book;

import java.util.ArrayList;

/**
 * Created by gjezzi on 22/06/16.
 */
public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private ArrayList<Book> mBooks;
    private Context mContext;

    public BookAdapter(Context context, ArrayList<Book> books) {
        this.mContext = context;
        this.mBooks = books;

    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        //private final Context mContext;
        public final CardView mBookCardView;
        public final LinearLayout mBooksLayout;
        public final TextView mAuthorName;
        public final TextView mBookTitle;
        public final TextView mBookDesc;
        public final ImageView mBookImage;

        public BookViewHolder(View view) {
            super(view);

            //this.mContext = context;
            mBookCardView = (CardView) view.findViewById(R.id.book_card_view);
            mBooksLayout = (LinearLayout) view.findViewById(R.id.book_list_item_layout);
            mAuthorName = (TextView) view.findViewById(R.id.author_title);
            mBookTitle = (TextView) view.findViewById(R.id.book_title);
            mBookDesc = (TextView) view.findViewById(R.id.book_description);
            mBookImage = (ImageView) view.findViewById(R.id.book_thumbnail);
        }
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.book_list_item, viewGroup, false);
        BookViewHolder bookViewHolder = new BookViewHolder(view);
        view.setFocusable(true);
        return bookViewHolder;
    }

    @Override
    public void onBindViewHolder(final BookViewHolder booksViewHolder, final int position){
        final Book books = mBooks.get(position);
        StringBuilder builder = new StringBuilder();

        for (String string : books.getVolumeInfo().getAuthors()) {
            if(builder.length() > 0) {
                builder.append(", ");
            }
            builder.append(string);
        }

        booksViewHolder.mAuthorName.setText(builder.toString());
        booksViewHolder.mBookTitle.setText(books.getVolumeInfo().getTitle());
        booksViewHolder.mBookDesc.setText(books.getVolumeInfo().getDescription());

        Glide.with(mContext)
                .load(books.getVolumeInfo().getImageLinks().getThumbnail())
                .crossFade()
                .into(booksViewHolder.mBookImage);

        booksViewHolder.mBookCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Id: " + books.getId(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext.getApplicationContext(), BookDetailsActivity.class);
                //intent.putExtra(BookDetailsFragment.EXTRA_BOOK, mBooks);
                mContext.startActivity(intent);
            }
        });
    }

    private void showPopUpMenu(View view){
        PopupMenu popupMenu = new PopupMenu(mContext, view);
        MenuInflater menuInflater = popupMenu.getMenuInflater();
        menuInflater.inflate(R.menu.book_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new MenuItemClickListener());
    }

    class MenuItemClickListener implements PopupMenu.OnMenuItemClickListener{
        public MenuItemClickListener(){ }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            int id = menuItem.getItemId();
            if(id == R.id.action_add_book){
                Toast.makeText(mContext, "Add to Library", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        if ( null == mBooks ) return 0;
        return mBooks.size();
    }
}


