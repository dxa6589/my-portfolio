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
    TreeMap<TimeRange, Event> attendeeEvents = new TreeMap<>(TimeRange.ORDER_BY_START);

    //go through the events collection to find events with attendees from the request
    Iterator eventItr = events.iterator();
    while(eventItr.hasNext()) {
      Event event = (Event)eventItr.next();
      
      //go through each event's attendees
      Iterator attendeeItr = event.getAttendees().iterator();
      while(attendeeItr.hasNext()){
        String attendee = (String)attendeeItr.next();

        //check if each attendee is in the attendees list for the new request
        if (attendees.contains(attendee)) {
          attendeeEvents.put(event.getWhen(), event);
          break;
        }
      }
    }

    //At this point, attendeeEvents contains all events our guests are attending
    //ie all events that we have to schedule around
    //TimeRanges are all ranges that don't overlap attendeeEvents

    //Iterate through attendeeEvents by start time

      //What if one conflict overlaps another?

    ArrayList<TimeRange> validTimes = new ArrayList<>();
    Iterator itr = attendeeEvents.values().iterator();
    Event cursor;
    int start = TimeRange.START_OF_DAY;

    //While ! last conflict
    while (itr.hasNext()) {
      cursor = (Event)itr.next();
      TimeRange check = TimeRange.fromStartDuration(start, (int)request.getDuration());
          System.out.println(cursor.getTitle() + " from " + cursor.getWhen().start() + " to " + cursor.getWhen().end() );

      //If conflict is present before duration is reached, 
      if (check.overlaps(cursor.getWhen())) {
        //restart from end of conflict
        start = cursor.getWhen().end();
        //check if it overlaps other events and set to the end of the last one
          System.out.println("A event " + cursor.getTitle() + " start " + cursor.getWhen().start());
        continue; 
      //Else, if there are no conflicts from start of day/end of last conflict to duration, 
      } else {
        //Add as timeRange ending at start of next conflict/end of day
        int end = cursor.getWhen().start();
          System.out.println("B event " + cursor.getTitle() + " start " + start + " end " + end);
        //checks if the next event starts after the current one ends
        if (end > start){
          TimeRange valid = TimeRange.fromStartEnd(start, end, false);
          validTimes.add(valid);
          start = cursor.getWhen().end();
        }
          System.out.println("C event " + cursor.getTitle() + " start " + start + " end " + end);
      }
    }
      System.out.println("start " + start + " duration " + request.getDuration());
    TimeRange last = TimeRange.fromStartEnd(start, TimeRange.END_OF_DAY, true);
    if (last.duration() >= request.getDuration()){
      validTimes.add(last);
    }
    return validTimes;
  }
}
