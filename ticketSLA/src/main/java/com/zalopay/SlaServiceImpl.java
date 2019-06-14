package com.zalopay;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;

/**
 * Created by thinhda.
 * Date: 2019-04-15
 */

public class SlaServiceImpl implements SlaService {
    @Override
    public Duration calculate(LocalDateTime begin, LocalDateTime end) {

        Duration duration = null;


        LocalTime timeBeginMor = LocalTime.of(8, 30); //8h30
        LocalTime timeBeginAft = LocalTime.of(13, 30); //13h30

        LocalTime timeEndMor = LocalTime.of(12, 0); //12h00
        LocalTime timeEndAft = LocalTime.of(18, 0);//18h00

        Duration gap_1 = Duration.ofMinutes(90); //1h30 (12h00 -> 13h30)
        Duration gap_2 = Duration.ofMinutes(14 * 60 + 30);//14h30 (18h00 -> 8h30 tomorrow)

        LocalTime timeBegin = begin.toLocalTime();
        LocalTime timeEnd = end.toLocalTime();

        int n = 0; //gap redundancy

        //reset time if
        //begin before 8h30
        if (timeBegin.compareTo(timeBeginMor) < 0) {
            //reset to 8h30
            begin = LocalDateTime.of(begin.toLocalDate(), timeBeginMor);
        }
        //begin after 12h00 and before 13h30
        else if (timeBegin.compareTo(timeEndMor) > 0 && timeBegin.compareTo(timeBeginAft) < 0) {
            //reset to 13h30
            begin = LocalDateTime.of(begin.toLocalDate(), timeBeginAft);
        }

        //if time begin is 8h30 -> 12h00 and time end is after 13h30 -> n++
        if (timeBegin.compareTo(timeEndMor) <= 0 && timeEnd.compareTo(timeBeginAft) >= 0) {
            n++;
        }

        duration = Duration.between(begin, end);

        //response in day
        if (begin.toLocalDate().equals(end.toLocalDate())) {
            duration = duration.minus(gap_1.multipliedBy(n));
        } else {
            //(01/01 9h30 -> 02/01 8h30) n++
            //(01/01 14h30 -> 02/01 13h30) n++
            //(01/01 9h30 -> 02/01 14h30) n++
            n++;

            long days = duration.toDays();

            //different day -> days must be 1
            //days = 0 because time < 24H, we need assign days to 1
            if (days == 0) days = 1;

            // duration =
            // + duration(begin, end)
            // - duration(12h00 -> 13h30) x (days - 1 + gap_redundancy)
            // - duration(18h30 -> 8h30 tomorrow) x (days)
            duration = duration.minus(gap_1.multipliedBy(days - 1 + n)).minus(gap_2.multipliedBy(days));
        }

        return duration;
    }
}
