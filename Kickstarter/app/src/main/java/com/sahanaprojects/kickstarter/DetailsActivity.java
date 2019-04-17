package com.sahanaprojects.kickstarter;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sahanaprojects.kickstarter.utils.DateTimeConverter;

public class DetailsActivity extends AppCompatActivity {

    TextView tvTitle, tvDesc, tvBy, tvPledge, tvBanker, tvLocation,
            tvPercentage, tvState, tvCurrency,tvType,tvEndTime, tvUrl;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackActivity();
            }
        });

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout ctl = findViewById(R.id.toolbar_layout);
        ctl.setTitle(getString(R.string.app_name));
        ctl.setExpandedTitleMarginBottom(5);
        ctl.setExpandedTitleMarginStart(5);
        ctl.setCollapsedTitleTextAppearance(R.style.coll_toolbar_title);
        ctl.setExpandedTitleTextAppearance(R.style.TransparentText);
        if(getIntent().getBundleExtra("BUNDLE")!=null) {
            tvBanker = findViewById(R.id.banker);
            imageView = findViewById(R.id.image);
            tvTitle = findViewById(R.id.tvTitle);
            tvUrl = findViewById(R.id.tvUrl);
            tvDesc = findViewById(R.id.tvDescription);
            tvBy = findViewById(R.id.tvBy);
            tvPledge = findViewById(R.id.pledges);
            tvPercentage = findViewById(R.id.percentage);
            tvLocation = findViewById(R.id.tvLocation);
            tvState = findViewById(R.id.tvState);
            tvCurrency = findViewById(R.id.currency);
            tvType = findViewById(R.id.type);
            tvEndTime = findViewById(R.id.endtime);
            Bundle bundle = getIntent().getBundleExtra("BUNDLE");

            //Display the data from the intent
            tvTitle.setText(bundle.getString(getString(R.string.Title)));
            tvDesc.setText(bundle.getString(getString(R.string.Description)));
            tvBy.setText(bundle.getString(getString(R.string.by)));
            tvPercentage.setText(bundle.getString(getString(R.string.Percentage))+"%\n\nPERCENTAGE FUNDED");
            tvPledge.setText("$"+bundle.getString(getString(R.string.Pledges))+"\n\nAMOUNT PLEDGED");
            tvCurrency.setText(bundle.getString(getString(R.string.Currency)));
            tvBanker.setText(bundle.getString(getString(R.string.Bancker))+"\n\nNUMBER OF BANCKER");
            tvLocation.setText(bundle.getString(getString(R.string.Location)));
            tvState.setText(bundle.getString(getString(R.string.state)));
            tvType.setText(bundle.getString(getString(R.string.type)));
            tvEndTime.setText(DateTimeConverter.convertDateTimetoDate(bundle.getString(getString(R.string.endTime))));
            tvUrl.setText(bundle.getString(getString(R.string.Image)));
            imageView.setImageResource(R.drawable.projectimage);
        }

    }

    //Go back to previous main activity
    private void BackActivity() {
        Intent back = new Intent(this,MainActivity.class);
        back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(back);

    }
}
