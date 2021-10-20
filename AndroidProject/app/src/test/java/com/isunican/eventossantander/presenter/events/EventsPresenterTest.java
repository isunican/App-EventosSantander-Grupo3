package com.isunican.eventossantander.presenter.events;

import com.isunican.eventossantander.model.Event;
import com.isunican.eventossantander.view.events.IEventsContract;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class EventsPresenterTest {

    @Before
    public void init() {
        EventsPresenter eventsPresenter = Robolectric.buildActivity(EventsPresenter.class);
    }

    @Test
    public void testOrdenar() {

    }
}
