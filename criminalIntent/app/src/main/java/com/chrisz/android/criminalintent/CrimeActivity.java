package com.chrisz.android.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import java.util.UUID;

/**
 * use CrimePagerActivity, so enable this
 */
public class CrimeActivity extends SingleFragmentActivity {

    public static final String EXTRA_CRIME_ID = "com.chrisz.android.criminalintent.crime_id";

    @Override
    protected Fragment createFragment() {
//        return new CrimeFragment();
        UUID crimeid = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        return CrimeFragment.newInstance(crimeid);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static Intent newIntent(Context pakageContext, UUID crimeid){
        Intent intent = new Intent(pakageContext, CrimeActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeid);
        return intent;
    }

}
