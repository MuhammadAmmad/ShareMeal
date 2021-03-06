package com.example.kristijan.sharemeal;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.firebase.client.Firebase;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kristijan on 19/05/16.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private Firebase mRef;
    private String mUserId;

    //private boolean showBackArrow;

    //String email = "";
    //String userName = "";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());

        authenticate();
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        makeDrawer();




    }

    protected abstract int getLayoutResourceId();


    protected String getmUserId(){
        return mUserId;
    }

    private void authenticate(){
        // Check Authentication
        mRef = new Firebase(Constants.FIREBASE_URL);
        if (mRef.getAuth() == null) {
            loadLoginView();
        }

        try {
            mUserId = mRef.getAuth().getUid();
        } catch (Exception e) {
            loadLoginView();
        }
    }

    protected void unauthenticate(){
        mRef.unauth();
        loadLoginView();
    }


    private void loadLoginView() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void loadUserSettings(){
        Intent intent = new Intent(this, UserSettings.class);
        startActivity(intent);
    }

    private void makeDrawer(){
        /*Firebase userDetails = new Firebase(Constants.FIREBASE_URL + "/users/" + getmUserId());
        userDetails.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                email = snapshot.child("email").getValue(String.class); //use in header
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });*/



        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(new ColorDrawable(0xFF3F51B5)) //change color
                .addProfiles(
                        new ProfileDrawerItem().withName("user").withEmail("email").withIcon(getResources().getDrawable(R.drawable.ic_person))
                )
                .withSelectionListEnabledForSingleProfile(false)
                .withOnAccountHeaderProfileImageListener(new AccountHeader.OnAccountHeaderProfileImageListener() {
                    @Override
                    public boolean onProfileImageClick(View view, IProfile profile, boolean current) {
                        loadUserSettings();
                        return true;
                    }

                    @Override
                    public boolean onProfileImageLongClick(View view, IProfile profile, boolean current) {
                        return false;
                    }

                })
                .build();

        PrimaryDrawerItem homeItem = new PrimaryDrawerItem().withName("Home");
        PrimaryDrawerItem hostingEventsItem = new PrimaryDrawerItem().withName("Hosting Events").withSelectable(true);
        PrimaryDrawerItem joinedEventsItem = new PrimaryDrawerItem().withName("Joined Events");
        //SecondaryDrawerItem item2 = new SecondaryDrawerItem();


        Drawer result = new DrawerBuilder() //if result not needed later, no need to store it
                .withActivity(this)
                .withAccountHeader(headerResult)
                .withTranslucentStatusBar(true)
                .withActionBarDrawerToggle(true)
                .withToolbar(toolbar)
                .addDrawerItems(
                        homeItem,
                        hostingEventsItem,
                        joinedEventsItem
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        drawerItem.withSetSelected(true);
                        switch (position) {
                            case 1:  loadHome();
                                break;
                            case 2:  loadHostingEvents();
                                break;
                            case 3:  loadJoinedEvents();
                                break;
                            }
                        return true;
                        }
                    })
                .build();



        /*if (showBackArrow == true){
            result.getActionBarDrawerToggle().setDrawerIndicatorEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }*/

    }

    private void loadHostingEvents(){
        Intent intent = new Intent(this, HostingEventsActivity.class);
        startActivity(intent);
    }

    private void loadJoinedEvents(){
        Intent intent = new Intent(this, JoinedEventsActivity.class);
        startActivity(intent);
    }

    private void loadHome(){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

}
