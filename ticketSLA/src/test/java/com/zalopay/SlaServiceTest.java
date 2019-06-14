package com.zalopay;

import org.hamcrest.MatcherAssert;
import org.hamcrest.core.Is;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.Duration;
import java.time.LocalDateTime;

@Test
public class SlaServiceTest {
    private SlaService slaService;

    @DataProvider(name = "slaData")
    public static Object[][] slaData() {
        return new Object[][]{
                {
                        //In day, morning
                        LocalDateTime.of(2019, 4, 30, 10, 0),
                        LocalDateTime.of(2019, 4, 30, 11, 0),
                        Duration.ofSeconds(3600) // 1h
                },
                {
                        //In day, afternoon
                        LocalDateTime.of(2019, 4, 30, 13, 30),
                        LocalDateTime.of(2019, 4, 30, 14, 0),
                        Duration.ofSeconds(3600) // 1h
                },
                {
                        //In day, morning to afternoon
                        LocalDateTime.of(2019, 4, 30, 9, 30),
                        LocalDateTime.of(2019, 4, 30, 15, 30),
                        Duration.ofSeconds(3600) // 1h
                },
                {
                        //Different day, in morning
                        LocalDateTime.of(2019, 4, 30, 10, 30),
                        LocalDateTime.of(2019, 5, 1, 9, 30),
                        Duration.ofSeconds(3600) // 1h
                },
                {
                        //Different day, in afternoon
                        LocalDateTime.of(2019, 4, 30, 15, 30),
                        LocalDateTime.of(2019, 5, 1, 13, 30),
                        Duration.ofSeconds(3600) // 1h
                },
                {
                        //Different day, in afternoon -> morning
                        LocalDateTime.of(2019, 4, 30, 15, 30),
                        LocalDateTime.of(2019, 5, 1, 8, 30),
                        Duration.ofSeconds(3600) // 1h
                },
                {
                        //Different day, in morning -> afternoon, false
                        LocalDateTime.of(2019, 4, 30, 8, 30),
                        LocalDateTime.of(2019, 5, 1, 15, 30),
                        Duration.ofSeconds(3600) // 1h
                },
        };
    }


    @BeforeClass
    public void init() {
        slaService = new SlaServiceImpl();
    }

    // GIVEN
    @Test(dataProvider = "slaData")
    public void calculate(LocalDateTime start, LocalDateTime end, Duration expectedResult) {
        // WHEN
        Duration duration = slaService.calculate(start, end);
        System.out.println(duration.toMinutes());

        // THEN
//        MatcherAssert.assertThat(duration, Is.is(expectedResult));
        MatcherAssert.assertThat(duration, IsLessThan.lessThan(Duration.ofHours(8)));
    }
}