package ch.epfl.sweng.tutosaurus.model;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by albertochiappa on 17/12/16.
 */

public class MeetingModelTest {
    Meeting meeting = new Meeting();

    @Test
    public void canConstructMeetingrequest(){
        MeetingRequest meetingRequest = new MeetingRequest("uid", meeting, false, "noType", "Einstein");
        assertEquals("uid", meetingRequest.getUid());
        assertEquals(meeting, meetingRequest.getMeeting());
        assertEquals("noType", meetingRequest.getType());
        assertEquals("Einstein", meetingRequest.getFrom());
    }

    @Test
    public void canSetMeetingLocation(){
        meeting.setNameLocation("EPFL");
        assertEquals("EPFL", meeting.getNameLocation());
    }
}
