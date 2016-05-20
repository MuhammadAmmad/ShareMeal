package com.example.kristijan.sharemeal;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;

/**
 * Created by kristijan on 20/05/16.
 */
public class UserSettings extends BaseActivity{

    @BindView(R.id.logoutBtn)
    Button logoutBtn;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_user_settings;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unauthenticate();
            }
        });
    }
}
