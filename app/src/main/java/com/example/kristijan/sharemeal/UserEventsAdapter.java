package com.example.kristijan.sharemeal;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by kristijan on 17/05/16.
 */
public class UserEventsAdapter extends RecyclerView.Adapter<UserEventsAdapter.ViewHolder> {



    private Context context;
    private List<String> events;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView titleTextView;
        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.title);//use @bind here?
        }
    }


    public UserEventsAdapter(Context context) {
        this.context = context;
        this.events = Collections.emptyList();
    }


    public void setEvents(List<String> events) {
        this.events = events;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    //@Override
    public Object getItem(int arg0) {
        return events.get(arg0);
    }

    /*@Override
    public long getItemId(int arg0) {
        return events.get(arg0).getId();
    }*/

    // Create new views (invoked by the layout manager)
    @Override
    public UserEventsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        // create a new view
        View v = LayoutInflater.from(context)
                .inflate(R.layout.view_cell_event, parent, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        // try using glide
        holder.titleTextView.setText(events.get(position));

    }


}