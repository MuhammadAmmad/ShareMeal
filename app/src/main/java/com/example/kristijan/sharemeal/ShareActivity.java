package com.example.kristijan.sharemeal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kristijan on 12/07/16.
 */
public class ShareActivity extends AppCompatActivity {

    @BindView(R.id.shareButton)
    Button shareButton;

    //@Override
    //protected int getLayoutResourceId() {
    //    return R.layout.activity_share;
    //}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        ButterKnife.bind(this);

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "I'm going to ___, join me with [app link]";
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, message);

                startActivity(Intent.createChooser(share, "Share with:"));
            }
        });
    }



}
