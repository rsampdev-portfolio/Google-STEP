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

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public final class FindMeetingQuery {
    
    public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
        List<TimeRange> openMeetingSlots;

        Collection<String> attendees = request.getAttendees();
        long duration = request.getDuration();

        if (attendees.size() == 0) {
            openMeetingSlots = new ArrayList<>();
            openMeetingSlots.add(TimeRange.WHOLE_DAY);
        } else if (duration < 0 || duration > TimeRange.WHOLE_DAY.duration()) {
            openMeetingSlots = new ArrayList<>();
        } else {
            openMeetingSlots = new ArrayList<>();
            openMeetingSlots.add(TimeRange.WHOLE_DAY);

            for (Event event : events) {

                for (int index = 0; index < openMeetingSlots.size(); index++) {
                    TimeRange range = openMeetingSlots.get(index);
                    TimeRange when = event.getWhen();
                    TimeRange secondHalf = null;
                    TimeRange firstHalf = null;

                    if (range.contains(when)) {
                        System.out.println("\n\n\n");
                        System.out.println("CONFLICT");
                        System.out.println("\n\n\n");

                        firstHalf = TimeRange.fromStartEnd(range.start(), when.start(), false);
                        secondHalf = TimeRange.fromStartEnd(when.end(), range.end(), false);


                        System.out.println(firstHalf);
                        System.out.println(when);
                        System.out.println(secondHalf);

                        index = openMeetingSlots.indexOf(range);
                    }

                    if (firstHalf != null && secondHalf != null) {
                        openMeetingSlots.remove(index);
                        openMeetingSlots.add(index, secondHalf);
                        openMeetingSlots.add(index, firstHalf);
                    }
                }            

            }

            // throw new UnsupportedOperationException("TODO: Implement this method.");
        }
        
        if (openMeetingSlots == null) {
            openMeetingSlots = new ArrayList<>();
        }

        return openMeetingSlots;
    }

}
