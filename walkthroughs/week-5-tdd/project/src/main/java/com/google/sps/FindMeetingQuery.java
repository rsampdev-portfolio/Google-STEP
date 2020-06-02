// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public final class FindMeetingQuery {
    
    public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
        List<TimeRange> openMeetingSlots = new ArrayList<>();
        Collection<String> attendees = request.getAttendees();
        long duration = request.getDuration();

        events = removeIrrelevantEvents(events, attendees);

        boolean outOfBoundsDuration = (duration < 0 || duration > TimeRange.WHOLE_DAY.duration());
        
        if (attendees.isEmpty()) {
            openMeetingSlots.add(TimeRange.WHOLE_DAY);
        } else if (!outOfBoundsDuration) {
            Collection<TimeRange> whens = getSmoothedTimeRangesFromEvents(events);
            openMeetingSlots.add(TimeRange.WHOLE_DAY);

            for (TimeRange when : whens) {
                for (int index = 0; index < openMeetingSlots.size(); index++) {
                    TimeRange range = openMeetingSlots.get(index);
                    TimeRange secondHalf = null;
                    TimeRange firstHalf = null;

                    if (range.contains(when)) {
                        firstHalf = TimeRange.fromStartEnd(range.start(), when.start(), false);
                        secondHalf = TimeRange.fromStartEnd(when.end(), range.end(), false);
                        index = openMeetingSlots.indexOf(range);
                    }

                    if (firstHalf != null && secondHalf != null) {
                        openMeetingSlots.remove(index);
                        openMeetingSlots.add(index, secondHalf);
                        openMeetingSlots.add(index, firstHalf);
                    }
                }
            }
        }

        openMeetingSlots = removeOpenPointMeetingSlots(openMeetingSlots);

        return openMeetingSlots;
    }

    private Collection<Event> removeIrrelevantEvents(Collection<Event> eventsCollection, Collection<String> attendees) {
        ArrayList<Event> events = new ArrayList<>();

        for (Event event : eventsCollection) {
            Set<String> eventAttendees = event.getAttendees();
            
            for (String attendee : attendees) {
                if (eventAttendees.contains(attendee)) {
                    events.add(event);
                    continue;
                }
            }
        }

        return events;
    }

    private Collection<TimeRange> getSmoothedTimeRangesFromEvents(Collection<Event> events) {
        ArrayList<TimeRange> rangesBuffer = new ArrayList<>();
        boolean flag = true;

        for (Event event : events) {
            rangesBuffer.add(event.getWhen());
        }

        while (flag) {
            flag = false;

            for (int index = 0; index < rangesBuffer.size() - 1 && flag == false; index++) {
                TimeRange next = rangesBuffer.get(index + 1);
                TimeRange current = rangesBuffer.get(index);
                flag = true;

                if (current.contains(next)) {
                    rangesBuffer.remove(next);
                } else if (next.contains(current)) {
                    rangesBuffer.remove(current);
                } else if (current.overlaps(next)) {
                    TimeRange resolved = TimeRange.fromStartEnd(current.start(), next.end(), false);
                    rangesBuffer.remove(index);
                    rangesBuffer.remove(index);
                    rangesBuffer.add(index, resolved);
                } else {
                    flag = false;
                }
            }
        }
        
        return rangesBuffer;
    }

    private List<TimeRange> removeOpenPointMeetingSlots(List<TimeRange> openMeetingSlots) {
        List<TimeRange> meetingsBuffer = new ArrayList<>();

        for (TimeRange meetingSlot : openMeetingSlots) {
            if (meetingSlot.duration() > 0) {
                meetingsBuffer.add(meetingSlot);
            }
        }

        return meetingsBuffer;
    }

}
