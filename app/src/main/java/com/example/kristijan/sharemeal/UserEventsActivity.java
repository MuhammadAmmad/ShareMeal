package com.example.kristijan.sharemeal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kristijan on 14/05/16.
 */
public class UserEventsActivity extends AppCompatActivity  {
    private Firebase mRef;

    private String mUserId;
    ArrayList<String> events;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_events);

        events = new ArrayList<>();

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

        ButterKnife.bind(this);


        final UserEventsAdapter adapter = new UserEventsAdapter(getApplicationContext());

        /*adapter.setOnItemClickListener(new RecipesAdapter.OnItemClickListener() {
            @Override
            public void onNoteItemClicked(Recipe recipe) {
                RecipesFragment.this.showRecipe(recipe.getId());
            }
        });*/
        adapter.setEvents(events);
        /*RecyclerItemClickSupport.addTo(recyclerView).setOnItemClickListener(new RecyclerItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                RecipesFragment.this.showRecipe(recipes.get(position).getId());//use intent instead? what's the best practice, if so how?

            }
        });*/

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplication()));
        recyclerView.setHasFixedSize(true);//increases performance if size constant
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        new Firebase(Constants.FIREBASE_URL + "/users/" + mUserId + "/ownsEvents")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        events.add(dataSnapshot.getValue(String.class));
                        System.out.println(dataSnapshot.getValue(String.class));
                        adapter.notifyDataSetChanged();
                        System.out.println(adapter.getItemCount());


                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });



    }

    private void loadLoginView() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
