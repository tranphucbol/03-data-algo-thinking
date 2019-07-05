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
                        Duration.ofMinutes(60) // 1h
                },
                {
                        //In day, afternoon
                        LocalDateTime.of(2019, 4, 30, 13, 30),
                        LocalDateTime.of(2019, 4, 30, 14, 0),
                        Duration.ofMinutes(30) // 30m
                },
                {
                        //In day, morning to afternoon
                        LocalDateTime.of(2019, 4, 30, 9, 30),
                        LocalDateTime.of(2019, 4, 30, 15, 30),
                        Duration.ofMinutes(4 * 60 + 30) // 4h30m
                },
                {
                        //Different day, in morning
                        LocalDateTime.of(2019, 4, 30, 10, 30),
                        LocalDateTime.of(2019, 5, 1, 9, 30),
                        Duration.ofMinutes(7 * 60) // 7h
                },
                {
                        //Different day, in afternoon
                        LocalDateTime.of(2019, 4, 30, 15, 30),
                        LocalDateTime.of(2019, 5, 1, 13, 30),
                        Duration.ofMinutes(6 * 60) // 6h
                },
                {
                        //Different day, in afternoon -> morning
                        LocalDateTime.of(2019, 4, 30, 15, 30),
                        LocalDateTime.of(2019, 5, 1, 8, 30),
                        Duration.ofMinutes(2 * 60 + 30) // 2h30m
                },
                {
                        //Different day, in morning -> afternoon
                        LocalDateTime.of(2019, 4, 30, 8, 30),
                        LocalDateTime.of(2019, 5, 1, 15, 30),
                        Duration.ofMinutes(13 * 60 + 30) // 13h30m
                },
                {
                        LocalDateTime.of(2019, 4, 30, 8, 30),
                        LocalDateTime.of(2019, 5, 3, 15, 30),
                        Duration.ofMinutes(13 * 60 + 30 + 2 * 8 * 60) // 13h30m
                },
                {
                        LocalDateTime.of(2019, 5, 5, 8, 30),
                        LocalDateTime.of(2019, 5, 6, 15, 30),
                        Duration.ofMinutes(5 * 60 + 30) // 13h30m
                },
                {
                        LocalDateTime.of(2019, 4, 30, 11, 30),
                        LocalDateTime.of(2019, 4, 30, 11, 45),
                        Duration.ofMinutes(15)
                },
                {
                        LocalDateTime.of(2019, 4, 30, 15, 30),
                        LocalDateTime.of(2019, 4, 30, 17, 45),
                        Duration.ofMinutes(135)
                },
                {
                        LocalDateTime.of(2019, 4, 30, 15, 00),
                        LocalDateTime.of(2019, 5, 1, 9, 00),
                        Duration.ofMinutes(210)
                },
                {
                        LocalDateTime.of(2019, 4, 30, 15, 00),
                        LocalDateTime.of(2019, 5, 3, 9, 00),
                        Duration.ofMinutes(210 + 480 * 2)
                },
                {
                        LocalDateTime.of(2019, 4, 30, 15, 00),
                        LocalDateTime.of(2019, 5, 10, 9, 00),
                        Duration.ofMinutes(210 + 480 * 9 - 270 - 480)
                }
                ,
                {
                        LocalDateTime.of(2019, 4, 30, 15, 00),
                        LocalDateTime.of(2019, 5, 13, 9, 00),
                        Duration.ofMinutes(210 + 480 * 12 - (270 + 480) * 2)
                }
                ,
                {
                        LocalDateTime.of(2019, 4, 30, 9, 00),
                        LocalDateTime.of(2019, 5, 13, 15, 00),
                        Duration.ofMinutes(750 + 480 * 12 - (270 + 480) * 2)
                },
                {
                        LocalDateTime.of(2019, 4, 30, 9, 00),
                        LocalDateTime.of(2019, 5, 13, 9, 00),
                        Duration.ofMinutes(480 + 480 * 12 - (270 + 480) * 2)
                }
                ,
                {
                        LocalDateTime.of(2019, 4, 30, 9, 00),
                        LocalDateTime.of(2019, 5, 13, 9, 30),
                        Duration.ofMinutes(480 + 480 * 12 - (270 + 480) * 2 + 30)
                }
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

        // THEN
        MatcherAssert.assertThat(duration, Is.is(expectedResult));
//        MatcherAssert.assertThat(duration, IsLessThan.lessThan(Duration.ofHours(8)));
    }
}