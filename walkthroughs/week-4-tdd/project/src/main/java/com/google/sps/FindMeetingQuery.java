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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeMap;

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    //throw new UnsupportedOperationException("TODO: Implement this method.");

    Collection<String> attendees = request.getAttendees();
    TreeMap<TimeRange, Event> conflicts = 
        new TreeMap<>(TimeRange.ORDER_BY_START);

    //go through the events collection to find events with attendees from the
    //request ie potential conflicts
    Iterator eventItr = events.iterator();
    while(eventItr.hasNext()) {
      Event event = (Event)eventItr.next();
      
      //go through each event's attendees
      Iterator attendeeItr = event.getAttendees().iterator();
      while(attendeeItr.hasNext()){
        String attendee = (String)attendeeItr.next();

        //check if each attendee is in the attendees list for the new request
        if (attendees.contains(attendee)) {
          conflicts.put(event.getWhen(), event);
          break;
        }
      }
    }

    //Iterate through conflicts by start time
    ArrayList<TimeRange> validTimes = new ArrayList<>();
    Iterator conflictsItr = conflicts.values().iterator();
    Event cursor;
    int start = TimeRange.START_OF_DAY;

    while (conflictsItr.hasNext()) {
      cursor = (Event)conflictsItr.next();
      int duration = (int)request.getDuration();
      TimeRange check = TimeRange.fromStartDuration(start, duration);

      //If conflict is present before duration is reached, 
      if (check.overlaps(cursor.getWhen())) {
        //restart from end of current conflict
        start = cursor.getWhen().end();
        continue; 
      //Else, if there are no conflicts from end of last conflict to duration,
      } else {
        int end = cursor.getWhen().start();
        //checks that the current event starts after the previous one ends
        if (end > start){
          //add valid time range from end of previous event to start of current 
          //event
          TimeRange valid = TimeRange.fromStartEnd(start, end, false);
          validTimes.add(valid);
          start = cursor.getWhen().end();
        }
      }
    }
    //If leftover time after final conflict is long enough, also add it as a 
    //valid timerange
    TimeRange last = TimeRange.fromStartEnd(start, TimeRange.END_OF_DAY, true);
    if (last.duration() >= request.getDuration()){
      validTimes.add(last);
    }
    return validTimes;
  }
}
