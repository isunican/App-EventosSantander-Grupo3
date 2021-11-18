package com.isunican.eventossantander.view.events;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.isunican.eventossantander.R;
import com.isunican.eventossantander.model.Event;
import com.isunican.eventossantander.view.common.CommonArrayAdapter;


import java.util.List;

public class EventArrayAdapter extends ArrayAdapter<Event> {

    private final List<Event> events;

    public EventArrayAdapter(@NonNull EventsActivity activity, int resource, @NonNull List<Event> objects) {
        super(activity, resource, objects);
        this.events = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Event event = events.get(position);

        // Create item view
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint({"ViewHolder", "InflateParams"}) View view = inflater.inflate(R.layout.events_listview_item, null);

        return CommonArrayAdapter.getView(event, view, this);
    }
}
