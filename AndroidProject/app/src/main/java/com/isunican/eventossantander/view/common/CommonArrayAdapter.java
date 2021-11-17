package com.isunican.eventossantander.view.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.isunican.eventossantander.R;
import com.isunican.eventossantander.model.Event;
import com.isunican.eventossantander.view.events.EventsActivity;
import com.isunican.eventossantander.view.today.TodayEventsActivity;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class CommonArrayAdapter extends ArrayAdapter<Event> {

    private final List<Event> events;

    public CommonArrayAdapter(@NonNull EventsActivity activity, int resource, @NonNull List<Event> objects) {
        super(activity, resource, objects);
        this.events = objects;
    }

    /**
     * Determines the image resource id that must be used as the icon for a given event.
     * @param event
     * @return the image resource id for the event
     */
    private int getImageIdForEvent(Event event) {
        int id = getContext().getResources().getIdentifier(
                getNormalizedCategory(event),
                "drawable",
                getContext().getPackageName());

        // fallback image in case of unrecognized category
        if (id == 0) {
            id = getContext().getResources().getIdentifier(
                    "otros",
                    "drawable",
                    getContext().getPackageName());
        }
        return id;
    }

    private static String getNormalizedCategory(Event event) {
        return StringUtils.deleteWhitespace(
                StringUtils.stripAccents(StringUtils.remove(event.getCategoria(),"/")))
                .toLowerCase();
    }
}
