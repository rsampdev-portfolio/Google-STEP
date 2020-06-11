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
import java.util.Arrays;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public final class FindMeetingQuery {
    
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    Collection<String> optionalAttendees = request.getOptionalAttendees();
    Collection<String> primaryAttendees = request.getAttendees();
    List<TimeRange> openMeetingSlots = new ArrayList<>();
    long duration = request.getDuration();

    boolean durationIsOutOfBounds = duration < 0 || duration > TimeRange.WHOLE_DAY.duration();

    if (durationIsOutOfBounds) {
      return Collections.emptyList();
    } else if (primaryAttendees.isEmpty()) {
      openMeetingSlots = List.of(TimeRange.WHOLE_DAY);
    }

    if (!optionalAttendees.isEmpty()) {
    	Set<String> allAttendees = new HashSet<>();
    	allAttendees.addAll(primaryAttendees);
      allAttendees.addAll(optionalAttendees);

      List<Event> tempEvents = removeIrrelevantEvents(events, allAttendees);
      openMeetingSlots = getOpenMeetingSlots(tempEvents, duration);            
		}

    if (openMeetingSlots.isEmpty()) {
    	if (primaryAttendees.isEmpty()) {
				return Collections.emptyList();
      } else {
        events = removeIrrelevantEvents(events, primaryAttendees);
        openMeetingSlots = getOpenMeetingSlots(events, duration);
      }
    }

    return openMeetingSlots;
  }
    
  private List<Event> removeIrrelevantEvents(Collection<Event> eventsCollection, Collection<String> attendees) {
    List<Event> events = new ArrayList<>();

    for (Event event : eventsCollection) {
    	Set<String> eventAttendees = event.getAttendees();
            
    	for (String attendee : attendees) {
    		if (eventAttendees.contains(attendee)) {
					events.add(event);
          break;
      	}
    	}
    }
    
		return events;
  }

  private List<TimeRange> getOpenMeetingSlots(Collection<Event> events, long duration) {
  	List<TimeRange> whens = combineNestedAndOverlappingEventTimeRanges(events);
   	List<TimeRange> openMeetingSlots = new ArrayList<>();
    openMeetingSlots.add(TimeRange.WHOLE_DAY);

    for (TimeRange when : whens) {
      for (int index = 0; index < openMeetingSlots.size(); index++) {
      	TimeRange range = openMeetingSlots.get(index);
        TimeRange secondHalf = null;
        TimeRange firstHalf = null;

        if (range.contains(when)) {
          firstHalf = TimeRange.fromStartEnd(range.start(), when.start(), false);
          secondHalf = TimeRange.fromStartEnd(when.end(), range.end(), false);
        }

        if (firstHalf != null && secondHalf != null) {
        	openMeetingSlots.remove(index);
          openMeetingSlots.add(index, secondHalf);
          openMeetingSlots.add(index, firstHalf);
        }
      }
    }
    
    openMeetingSlots = openMeetingSlots.stream().filter(meetingSlot -> meetingSlot.duration() >= duration).collect(Collectors.toList());

    return openMeetingSlots;
  }

  private List<TimeRange> combineNestedAndOverlappingEventTimeRanges(Collection<Event> events) {
  	List<TimeRange> rangesBuffer = new ArrayList<>();
    boolean foundNestedOrOverlappingTimeRange = true;

    for (Event event : events) {
    	rangesBuffer.add(event.getWhen());
		}

    Collections.sort(rangesBuffer, TimeRange.ORDER_BY_START);

    while (foundNestedOrOverlappingTimeRange) {
			foundNestedOrOverlappingTimeRange = false;

      for (int index = 0; index < rangesBuffer.size() - 1 && foundNestedOrOverlappingTimeRange == false; index++) {
      	TimeRange next = rangesBuffer.get(index + 1);
        TimeRange current = rangesBuffer.get(index);
        foundNestedOrOverlappingTimeRange = true;

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
        	foundNestedOrOverlappingTimeRange = false;
        }
      }
    }

    return rangesBuffer;
  }
    
}
