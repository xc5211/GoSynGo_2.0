package edu.scu.util;

import java.sql.Timestamp;
import java.util.Comparator;

/**
 * Created by chuanxu on 4/13/16.
 */
public class TimestampComparator implements Comparator<Timestamp> {

    @Override
    public int compare(Timestamp timestamp1, Timestamp timestamp2) {
        if (timestamp1.getTime() > timestamp2.getTime()) {
            return 1;
        } else {
            return -1;
        }
    }

}
