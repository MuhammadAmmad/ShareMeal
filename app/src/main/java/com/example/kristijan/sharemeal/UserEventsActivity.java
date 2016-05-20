package com.example.kristijan.sharemeal;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by kristijan on 14/05/16.
 */
public class UserEventsActivity extends BaseActivity  {
    private Firebase mRef;

    ArrayList<String> events;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_user_events;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        events = new ArrayList<>();




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

        new Firebase(Constants.FIREBASE_URL + "/users/" + getmUserId() + "/ownsEvents")
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


}
