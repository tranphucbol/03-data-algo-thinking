package com.zalopay;

import java.time.*;

/**
 * Created by thinhda.
 * Date: 2019-04-15
 */

public class SlaServiceImpl implements SlaService {
    private static final LocalTime timeBeginMor = LocalTime.of(8, 30); //8h30
    private static final LocalTime timeBeginAft = LocalTime.of(13, 30); //13h30
    private static final LocalTime timeEndMor = LocalTime.of(12, 0); //12h00
    private static final LocalTime timeEndAft = LocalTime.of(18, 0);//18h00

    @Override
    public Duration calculate(LocalDateTime begin, LocalDateTime end) {

        Duration duration = null;

        Duration gap_1 = Duration.ofMinutes(90); //1h30 (12h00 -> 13h30)
        Duration gap_2 = Duration.ofMinutes(14 * 60 + 30);//14h30 (18h00 -> 8h30 tomorrow)
        Duration gap_3 = Duration.ofHours(8);

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

        if(begin.getDayOfWeek().compareTo(DayOfWeek.SATURDAY) == 0) {
            begin = begin.plusDays(2);
        } else if(begin.getDayOfWeek().compareTo(DayOfWeek.SUNDAY) == 0) {
            begin = begin.plusDays(1);
        }

        //if time begin is 8h30 -> 12h00 and time end is after 13h30 -> n++
        if (timeBegin.compareTo(timeEndMor) <= 0 && timeEnd.compareTo(timeBeginAft) >= 0) {
            n++;
        } else if (timeBegin.compareTo(timeBeginAft) >= 0 && timeBegin.compareTo(timeEndAft) <= 0 &&
                timeEnd.compareTo(timeBeginMor) >= 0 && timeEnd.compareTo(timeEndMor) <= 0) {
            n--;
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
            long weekends =  (end.getDayOfYear() / 7 - begin.getDayOfYear() / 7) * 2;

            //different day -> days must be 1
            //days = 0 because time < 24H, we need assign days to 1
            if (days == 0) days = 1;

            // duration =
            // + duration(begin, end)
            // - duration(12h00 -> 13h30) x (days - 1 + gap_redundancy)
            // - duration(18h -> 8h30 tomorrow) x (days)
            // - duration(8h (1 work day)) x (weekends)
            duration = duration
                    .minus(gap_1.multipliedBy(days - 1 + n))
                    .minus(gap_2.multipliedBy(days))
                    .minus(gap_3.multipliedBy(weekends));
        }

        return duration;
    }

}
