package com.zalopay;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class App {
    public static void main(String[] args) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime begin = LocalDateTime.of(2019, 4, 30, 10, 30);
        LocalDateTime end = LocalDateTime.of(2019, 4, 30, 14, 30);
        Duration duration = Duration.between(begin, end);
        duration = duration.minus(Duration.ofMinutes(90));
        long minutes = duration.toMinutes();
    }
}
