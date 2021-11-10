package com.isunican.eventossantander.view.events;

import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;
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

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class EventArrayAdapter extends ArrayAdapter<Event> implements Parcelable {

    private final List<Event> events;
    private final EventsActivity activity;

    public EventArrayAdapter(@NonNull EventsActivity activity, int resource, @NonNull List<Event> objects) {
        super(activity, resource, objects);
        this.activity = activity;
        this.events = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Event event = events.get(position);

        // Create item view
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.events_listview_item, null);

        // Link subviews
        TextView titleTxt = view.findViewById(R.id.item_event_title);
        TextView dateTxt = view.findViewById(R.id.item_event_date);
        ImageView imageTxt = view.findViewById(R.id.item_event_image);

        // Assign values to TextViews
        titleTxt.setText(event.getNombre());
        dateTxt.setText(event.getFecha());

        // Assign image
        imageTxt.setImageResource(getImageIdForEvent(event));

        return view;
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
                StringUtils.stripAccents(event.getCategoria()))
                .toLowerCase();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    /**
     * Guarga los objetos en el Parcel
     */
    public void writeToParcel(Parcel out, int flags) {
        out.writeList(events);
        out.write
    }

    public static final Parcelable.Creator<EventArrayAdapter> CREATOR
            = new Parcelable.Creator<EventArrayAdapter>() {
        public EventArrayAdapter createFromParcel(Parcel in) {
            List<Event> eventsParcel = null;
            in.readList(eventsParcel, ArrayList.class.getClassLoader());
            return new EventArrayAdapter(EventsActivity.class, 0, eventsParcel);
        }

        public EventArrayAdapter[] newArray(int size) {
            return new EventArrayAdapter[size];
        }
    };
//
//    /**
//     * Devuelve los objetos desde el parcel
//     * @param in
//     * @param activity
//     */
//    private EventArrayAdapter(Parcel in ) {
//
//        events = in.readList(events, null);
//        this.activity = activity;
//
//
//    }
}
