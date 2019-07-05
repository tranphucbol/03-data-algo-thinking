package com.zalopay;

import java.time.*;
import java.util.Random;

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

        Duration breakTime = Duration.ofMinutes(90); //1h30 (12h00 -> 13h30)

        LocalTime timeBegin = begin.toLocalTime();
        LocalTime timeEnd = end.toLocalTime();


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
        } else if(timeBegin.compareTo(timeEndAft) > 0) {
            //reset to 8h30 tomorrow
            begin = LocalDateTime.of(begin.toLocalDate().plusDays(1), timeBeginMor);
        }

        if(begin.getDayOfWeek().compareTo(DayOfWeek.SATURDAY) == 0) {
            if(timeBegin.compareTo(timeEndMor) > 0) {
                begin = begin.plusDays(2);
                begin = LocalDateTime.of(begin.toLocalDate(), timeBeginMor);
            }
        }

        if(begin.getDayOfWeek().compareTo(DayOfWeek.SUNDAY) == 0) {
            begin = begin.plusDays(1);
            begin = LocalDateTime.of(begin.toLocalDate(), timeBeginMor);
        }

        duration = Duration.between(begin, end);

        //response in day
        if (begin.toLocalDate().equals(end.toLocalDate())) {
            if (isMorning(begin.toLocalTime()) && isAfternoon(end.toLocalTime())) {
                duration = duration.minus(breakTime);
            }
        } else {
            long days = duration.toDays();
            if(days > 0 && duration.toMinutes() % (24 * 60) < 12 * 60) {
                days--;
            }
            long weekends = (end.getDayOfYear() / 7 - begin.getDayOfYear() / 7);
            duration = Duration.ofMinutes((days - weekends) * 8 * 60 - weekends * (60 * 4 + 30));

            if(isMorning(begin.toLocalTime())) {
                duration = duration.plus(Duration.between(begin.toLocalTime(), timeEndAft).minus(breakTime));
            } else {
                duration = duration.plus(Duration.between(begin.toLocalTime(), timeEndAft));
            }

            if(isMorning(end.toLocalTime())) {
                duration = duration.plus(Duration.between(timeBeginMor, end.toLocalTime()));
            } else {
                duration = duration.plus(Duration.between(timeBeginMor, end.toLocalTime()).minus(breakTime));
            }
        }

        return duration;
    }

    private boolean isMorning(LocalTime time) {
        return (time.compareTo(timeBeginMor) >= 0 && time.compareTo(timeEndMor) <= 0);
    }
    private boolean isAfternoon(LocalTime time) {
        return (time.compareTo(timeBeginAft) >= 0 && time.compareTo(timeEndAft) <= 0);
    }
}
