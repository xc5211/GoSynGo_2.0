package edu.scu.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.scu.util.TimestampComparator;

/**
 * Created by chuanxu on 4/13/16.
 */
public class EventMemberDetail {

    private int status;    // set default to 0
    private boolean isCheckedIn;    // set default to false
    private int estimateInMin;  // set default to -1
    private List<Timestamp> proposedTimestamps;
    private List<Timestamp> selectedTimestamps;

    public EventMemberDetail() {

    }

    public int getStatus() {
        return this.status;
    }

    public boolean isCheckedIn() {
        return this.isCheckedIn;
    }

    public int getEstimateInMin() {
        return this.estimateInMin;
    }

    public List<Timestamp> getProposedTimestamps() {
        return this.proposedTimestamps;
    }

    public List<Timestamp> getSelectedTimestamps() {
        return this.selectedTimestamps;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setCheckedIn(boolean isCheckedIn) {
        this.isCheckedIn = isCheckedIn;
    }

    public void setEstimateInMin(int estimate) {
        this.estimateInMin = estimate;
    }

    public boolean containsProposedTimestamp(Timestamp timestamp) {
        return this.proposedTimestamps.contains(timestamp);
    }

    public boolean containsSelectedTimestamp(Timestamp timestamp) {
        return this.selectedTimestamps.contains(timestamp);
    }

    public boolean addProposedTimestamp(Timestamp timestamp) {
        if (!this.containsProposedTimestamp(timestamp)) {
            boolean success = this.proposedTimestamps.add(timestamp);
            if (!success) {
                return false;
            }
            Collections.sort(this.proposedTimestamps, new TimestampComparator());
        }
        return true;
    }

    public boolean addSelectedTimestamp(Timestamp timestamp) {
        if (!this.containsSelectedTimestamp(timestamp)) {
            this.selectedTimestamps.add(timestamp);
            Collections.sort(this.selectedTimestamps, new TimestampComparator());
        }
        return true;
    }

    public boolean removeProposedTimestamp(Timestamp timestamp) {
        if (this.containsProposedTimestamp(timestamp)) {
            return this.proposedTimestamps.remove(timestamp);
        }
        return true;
    }

    public boolean removeSelectedTimestamp(Timestamp timestamp) {
        if (this.containsSelectedTimestamp(timestamp)) {
            return this.selectedTimestamps.remove(timestamp);
        }
        return true;
    }

}
