package us.dot.its.jpo.geojsonconverter.converter;

import org.junit.Test;
import us.dot.its.jpo.asn.j2735.r2024.Common.MinuteOfTheYear;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class FieldConversionsTest {
    @Test
    public void testConvertMinuteOfYear() {
        // Normal case: middle of year
        ZonedDateTime ingestTime = ZonedDateTime.of(2025, 10, 3, 0, 0, 1, 0, ZoneOffset.UTC);
        final int dayOfYear = ingestTime.getDayOfYear();
        final int minuteOfYear = dayOfYear * 24 * 60 + 5;
        final var moy = new MinuteOfTheYear(minuteOfYear);
        final ZonedDateTime minuteDate = FieldConversions.convertMinuteOfYear(moy, ingestTime);
        final int year = minuteDate.getYear();
        assertThat(year, equalTo(2025));
    }

    @Test
    public void testConvertMinuteOfYear_InvalidValue() {
        ZonedDateTime ingestTime = ZonedDateTime.of(2025, 10, 3, 0, 0, 1, 500, ZoneOffset.UTC);
        final var moy = new MinuteOfTheYear(527040L);
        final ZonedDateTime minuteDate = FieldConversions.convertMinuteOfYear(moy, ingestTime);
        assertThat(minuteDate, nullValue());
    }


    @Test
    public void testConvertMinuteOfYear_YearEnd() {
        // Test edge case of message sent at the end of the year
        final var moy = new MinuteOfTheYear(525599);  // Last minute of the year

        // Last minute of this year
        final ZonedDateTime ingestTimeThisYear = ZonedDateTime.of(2022, 12, 31, 23, 59, 59, 500, ZoneOffset.UTC);
        final ZonedDateTime minuteDate1 = FieldConversions.convertMinuteOfYear(moy, ingestTimeThisYear);
        assertThat(minuteDate1, notNullValue());
        final int year1 = minuteDate1.getYear();
        assertThat(year1, equalTo(2022));

        // First minute of next year
        final ZonedDateTime ingestTimeNextYear = ZonedDateTime.of(2023, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
        final ZonedDateTime minuteDate2 = FieldConversions.convertMinuteOfYear(moy, ingestTimeNextYear);
        assertThat(minuteDate2, notNullValue());
        final int year2 = minuteDate2.getYear();
        assertThat(year2, equalTo(2022));

        final var moyLeap = new MinuteOfTheYear(527037L);
        // Last minute of leap year
        final ZonedDateTime ingestTimeThisLeapYear = ZonedDateTime.of(2024, 12, 31, 23, 59, 59, 500, ZoneOffset.UTC);
        final ZonedDateTime minuteDateLeap1 = FieldConversions.convertMinuteOfYear(moyLeap, ingestTimeThisLeapYear);
        assertThat(minuteDateLeap1, notNullValue());
        final int yearLeap1 = minuteDateLeap1.getYear();
        assertThat(yearLeap1, equalTo(2024));

        final ZonedDateTime ingestTimeNextLeapYear = ZonedDateTime.of(2025, 1, 1, 0, 0, 1, 500, ZoneOffset.UTC);
        final ZonedDateTime minuteDateLeap2 = FieldConversions.convertMinuteOfYear(moy, ingestTimeNextLeapYear);
        assertThat(minuteDateLeap2, notNullValue());
        final int yearLeap2 = minuteDateLeap2.getYear();
        assertThat(yearLeap2, equalTo(2024));
    }
}
