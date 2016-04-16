package edu.scu.model;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.scu.util.TimestampComparator;

/**
 * Created by chuanxu on 4/13/16.
 */
public class Event implements Serializable {

    private String objectId;
    private String name;
    private Text note;
    private Text location;
    private int durationInMin;
    private boolean hasReminder;
    private int reminderInMin;
    private Timestamp timestamp;
    private StatusEvent status;
    private Person leader;
    private List<Timestamp> proposedTimestamps;
    private Map<Person, EventMemberDetail> eventMembersMap;

    public Event(String name, int durationInMin, boolean hasReminder, int reminderInMin, Person leader) {
        this.name = name;
        this.note = null;
        this.location = null;
        this.durationInMin = durationInMin;
        this.hasReminder = hasReminder;
        this.reminderInMin = reminderInMin;
        this.timestamp = null;
        this.status = StatusEvent.Pending;
        this.leader = leader;
        this.proposedTimestamps = new ArrayList<>();
        this.eventMembersMap = new HashMap<>();
    }

    public Event(String name, Text note, int durationInMin, boolean hasReminder, int reminderInMin, Person leader) {
        this(name, durationInMin, hasReminder, reminderInMin, leader);
        this.note = note;
    }

    public Event(String name, Text note, Text location, int durationInMin, boolean hasReminder, int reminderInMin, Person leader) {
        this(name, note, durationInMin, hasReminder, reminderInMin, leader);
        this.location = location;
    }

    public String getObjectId() {
        return this.objectId;
    }

    public String getName() {
        return this.name;
    }

    public Text getNote() {
        return this.note;
    }

    public Text getLocation() {
        return this.location;
    }

    public int getDurationInMin() {
        return this.durationInMin;
    }

    public boolean hasReminder() {
        return this.hasReminder;
    }

    public int getReminderInMin() {
        return this.reminderInMin;
    }

    public Timestamp getTimestamp() {
        return this.timestamp;
    }

    public StatusEvent getStatus() {
        return this.status;
    }

    public List<Timestamp> getProposedTimestamps() {
        return this.proposedTimestamps;
    }

    public boolean containsProposedTimestamp(Timestamp timestamp) {
        return this.proposedTimestamps.contains(timestamp);
    }

    public boolean removeProposedTime(Timestamp timestamp) {
        if (this.containsProposedTimestamp(timestamp)) {
            return this.proposedTimestamps.remove(timestamp);
        }
        return true;
    }

    public Person getLeader() {
        return this.leader;
    }

    public Set<Person> getMembers() {
        return this.eventMembersMap.keySet();
    }

    public Collection<EventMemberDetail> getEventMemberDetails() {
        return this.eventMembersMap.values();
    }

    public boolean isLeader(Person person) {
        return this.leader == person;
    }

    public boolean hasMember(Person person) {
        return this.eventMembersMap.containsKey(person);
    }

    public EventMemberDetail getEventMemberDetail(Person person) {
        if (!this.hasMember(person)) {
            return null;
        }
        return this.eventMembersMap.get(person);
    }

    public StatusMember getMemberStatus(Person member) {
        if (!this.hasMember(member)) {
            return null;
        }
        return this.eventMembersMap.get(member).getStatus();
    }

    public boolean isMemberCheckedIn(Person member) {
        if (!this.hasMember(member)) {
            return false;
        }
        return this.eventMembersMap.get(member).isCheckedIn();
    }

    public int getMemberEstimateInMin(Person member) {
        if (!this.hasMember(member)) {
            return -1;
        }
        return this.eventMembersMap.get(member).getEstimateInMin();
    }

    public List<Timestamp> getMemberProposedTimestamps(Person member) {
        if (!this.hasMember(member)) {
            return null;
        }
        return this.eventMembersMap.get(member).getProposedTimestamps();
    }

    public List<Timestamp> getMemberSelectedTimestamps(Person member) {
        if (!this.hasMember(member)) {
            return null;
        }
        return this.eventMembersMap.get(member).getSelectedTimestamps();
    }

    public List<Person> getMembersAsPending() {
        List<Person> pendingMembers = new ArrayList<>();
        for (Map.Entry<Person, EventMemberDetail> memberInfo : this.eventMembersMap.entrySet()) {
            if (memberInfo.getValue().getStatus().equals(StatusMember.Pending)) {
                pendingMembers.add(memberInfo.getKey());
            }
        }
        return pendingMembers;
    }

    public List<Person> getMembersAsAccepted() {
        List<Person> readyMembers = new ArrayList<>();
        for (Map.Entry<Person, EventMemberDetail> memberInfo : this.eventMembersMap.entrySet()) {
            if (memberInfo.getValue().getStatus().equals(StatusMember.Accept)) {
                readyMembers.add(memberInfo.getKey());
            }
        }
        return readyMembers;
    }

    public List<Person> getMembersAsDeclined() {
        List<Person> declinedMembers = new ArrayList<>();
        for (Map.Entry<Person, EventMemberDetail> memberInfo : this.eventMembersMap.entrySet()) {
            if (memberInfo.getValue().getStatus().equals(StatusMember.Declined)) {
                declinedMembers.add(memberInfo.getKey());
            }
        }
        return declinedMembers;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNote(Text note) {
        this.note = note;
    }

    public void setLocation(Text location) {
        this.location = location;
    }

    public void setDurationInMin(int durationInMin) {
        this.durationInMin = durationInMin;
    }

    public boolean addProposedTime(Timestamp timestamp) {
        if (!this.containsProposedTimestamp(timestamp)) {
            boolean success = this.proposedTimestamps.add(timestamp);
            if (!success) {
                return false;
            }
            Collections.sort(this.proposedTimestamps, new TimestampComparator());
        }
        return true;
    }

    public void setHasReminder(boolean hasReminder) {
        this.hasReminder = hasReminder;
    }

    public void setReminderInMin(int reminderInMin) {
        this.reminderInMin = reminderInMin;
    }

    public void setEventTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public void setStatus(StatusEvent status) {
        this.status = status;
    }

    /**
     * This method is used for switching the leader of an event.
     * TODO: Later
     * @param leader
     * @return
     */
    public boolean switchLeader(Person leader) {
        if (!this.getMembers().contains(leader) || !leader.getEventsAsMemeber().contains(this)) {
            return false;
        }

        // update Person obj
        boolean oldLeaderUpdate =  this.getLeader().getEventsAsLeader().remove(this) &&
                this.getLeader().getEventsAsMemeber().add(this);
        boolean newLeaderUpdate = leader.getEventsAsLeader().add(this) &&
                leader.getEventsAsMemeber().remove(this);

        // update "this" event obj


        return oldLeaderUpdate && newLeaderUpdate;
    }

    public boolean addEventMember(Person member) {
        if (!this.eventMembersMap.containsKey(member)) {
            EventMemberDetail eventMemberDetail = new EventMemberDetail();
            this.eventMembersMap.put(member, eventMemberDetail);
        }
        return true;
    }

    public boolean removeEventMember(Person person) {
        if (this.hasMember(person)) {
            this.eventMembersMap.remove(person);
            return person.removeEventAsMember(this);
        }
        return true;
    }

}
