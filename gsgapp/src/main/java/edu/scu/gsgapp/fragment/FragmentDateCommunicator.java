package edu.scu.gsgapp.fragment;

import java.util.Date;
import java.util.List;

/**
 * Created by chuanxu on 6/3/16.
 */
public interface FragmentDateCommunicator {

    void updateLeaderSelectedDates(List<Date> leaderSelectedDates);
    void updateMemberSelectedDates(List<Date> memberSelectedDates);
}
