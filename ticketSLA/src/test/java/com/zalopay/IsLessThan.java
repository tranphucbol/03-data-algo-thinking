package com.zalopay;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.time.Duration;

public class IsLessThan extends TypeSafeMatcher {

    private Duration duration;

    public IsLessThan(Duration duration) {
        this.duration = duration;
    }

    @Override
    protected boolean matchesSafely(Object o) {
        Duration duration = (Duration) o;
        return (duration.compareTo(this.duration) <= 0);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("hello");
    }

    public static Matcher lessThan(Duration duration) {
        return new IsLessThan(duration);
    }
}
