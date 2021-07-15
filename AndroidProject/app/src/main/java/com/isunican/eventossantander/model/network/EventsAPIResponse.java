package com.isunican.eventossantander.model.network;

import com.google.gson.annotations.SerializedName;
import com.isunican.eventossantander.model.Event;

import java.util.List;

public class EventsAPIResponse {

    @SerializedName(value="resources")
    private List<Event> events;

    public List<Event> getEvents() {
        return events;
    }
    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
