package com.example.android.ireadalot.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.ireadalot.R;
import com.example.android.ireadalot.adapter.PagerAdapter;

public class MainActivity extends BaseActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        FragmentPagerAdapter fragmentPagerAdapter = new PagerAdapter(getSupportFragmentManager(), getApplicationContext());
        viewPager.setAdapter(fragmentPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.action_search_book, menu);
        return true;

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
        final AlertDialog.Builder searchDialog = new AlertDialog.Builder(getApplicationContext());
        searchDialog.setTitle(R.string.search_dialog_title);

        final EditText bookInput = new EditText(getApplicationContext());
        searchDialog.setView(bookInput);

        searchDialog.setPositiveButton("Search", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String userResult = bookInput.getText().toString();
                Toast.makeText(getApplicationContext(), "Searching Books", Toast.LENGTH_SHORT).show();
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
