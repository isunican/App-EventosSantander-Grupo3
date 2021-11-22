package com.isunican.eventossantander.view.common;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.isunican.eventossantander.R;
import com.isunican.eventossantander.model.Event;

import org.apache.commons.lang3.StringUtils;

public abstract class CommonArrayAdapter{

    public static View getView(Event event, View view,  ArrayAdapter<Event> arrayAdapter){
        // Link subviews
        TextView titleTxt = view.findViewById(R.id.item_event_title);
        TextView dateTxt = view.findViewById(R.id.item_event_date);
        ImageView imageTxt = view.findViewById(R.id.item_event_image);

        // Assign values to TextViews
        titleTxt.setText(event.getNombre());
        dateTxt.setText(event.getFecha());

        // Assign image
        imageTxt.setImageResource(CommonArrayAdapter.getImageIdForEvent(event, arrayAdapter));

        return view;
    }

    /**
     * Determines the image resource id that must be used as the icon for a given event.
     * @param event
     * @return the image resource id for the event
     */
    public static int getImageIdForEvent(Event event, ArrayAdapter<Event> arrayAdapter) {
        int id = arrayAdapter.getContext().getResources().getIdentifier(
                getNormalizedCategory(event),
                "drawable",
                arrayAdapter.getContext().getPackageName());

        // fallback image in case of unrecognized category
        if (id == 0) {
            id = arrayAdapter.getContext().getResources().getIdentifier(
                    "otros",
                    "drawable",
                    arrayAdapter.getContext().getPackageName());
        }
        return id;
    }

    public static String getNormalizedCategory(Event event) {
        return StringUtils.deleteWhitespace(
                StringUtils.stripAccents(StringUtils.remove(event.getCategoria(),"/")))
                .toLowerCase();
    }
}
