package com.isunican.eventossantander.model.network;

import retrofit2.Call;
import retrofit2.http.GET;

public interface EventsAPI {

    @GET("agenda_cultural.json")
    Call<EventsAPIResponse> getEventosResponse();
}
