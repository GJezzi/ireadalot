package com.example.android.ireadalot.adapter;

import android.content.Context;
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
import com.example.android.ireadalot.model.Book;

import java.util.ArrayList;

/**
 * Created by gjezzi on 22/06/16.
 */
public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private ArrayList<Book> mBooks = new ArrayList<>();
    private Context mContext;

    public BookAdapter(Context context, ArrayList<Book> books) {
        this.mContext = context;
        this.mBooks = books;

    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        private final Context mContext;
        public final LinearLayout mBooksLayout;
        public final TextView mAuthorName;
        public final TextView mBookTitle;
        public final TextView mBookDesc;
        public final ImageView mBookImage;
        //public final ImageView mOverflow;

        public BookViewHolder(Context context, View view) {
            super(view);

            this.mContext = context;
            mBooksLayout = (LinearLayout) view.findViewById(R.id.book_list_item_layout);
            mAuthorName = (TextView) view.findViewById(R.id.author_title);
            mBookTitle = (TextView) view.findViewById(R.id.book_title);
            mBookDesc = (TextView) view.findViewById(R.id.book_description);
            mBookImage = (ImageView) view.findViewById(R.id.book_thumbnail);
            //mOverflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.book_list_item, viewGroup, false);
        view.setFocusable(true);
        return new BookViewHolder(mContext, view);
    }

    @Override
    public void onBindViewHolder(final BookViewHolder booksViewHolder, int position){
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
        Glide.with(mContext).load(books.getVolumeInfo().getImageLinks().getThumbnail()).into(booksViewHolder.mBookImage);



//        booksViewHolder.mOverflow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showPopUpMenu(booksViewHolder.mOverflow);
//            }
//        });
    }

    private void showPopUpMenu(View view){
        PopupMenu popupMenu = new PopupMenu(mContext, view);
        MenuInflater menuInflater = popupMenu.getMenuInflater();
        menuInflater.inflate(R.menu.book_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new MenuItemClickListener());
    }

    class MenuItemClickListener implements PopupMenu.OnMenuItemClickListener{
        public MenuItemClickListener(){

        }
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


