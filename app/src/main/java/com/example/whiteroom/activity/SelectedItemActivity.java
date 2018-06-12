package com.example.whiteroom.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.whiteroom.R;
import com.example.whiteroom.fragment.SelectedItemFragment;


public class SelectedItemActivity extends AppCompatActivity {

    private static final String LOG_TAG = SelectedItemActivity.class.getSimpleName();

    @Override
    protected void onCreate( Bundle savedInstanceState ) {

        super.onCreate( savedInstanceState );

        setContentView( R.layout.activity_selected_item );

        Bundle bundle = getIntent().getExtras();
        int itemId = bundle.getInt( SelectedItemFragment.ARGUMENT_ITEMID );

        if ( savedInstanceState == null ) {

            getSupportFragmentManager().beginTransaction()
                    .add( R.id.detail_fl_container, SelectedItemFragment.newInstance( itemId ) )
                    .commit();

        }

    }
}
