package us.dot.its.jpo.geojsonconverter.converter;


import lombok.extern.slf4j.Slf4j;
import us.dot.its.jpo.asn.j2735.r2024.Common.*;
import us.dot.its.jpo.asn.j2735.r2024.SignalRequestMessage.DeltaTime;
import us.dot.its.jpo.geojsonconverter.pojos.common.ProcessedBasicVehicleRole;
import us.dot.its.jpo.geojsonconverter.pojos.common.ProcessedRequestImportanceLevel;
import us.dot.its.jpo.geojsonconverter.pojos.common.ProcessedRequestSubRole;
import us.dot.its.jpo.geojsonconverter.pojos.common.ProcessedVehicleType;

import java.time.*;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
public class FieldConversions {
    public static Double convertLong(long j2735Long) {
        // Longitude ::= INTEGER (-1799999999..1800000001)
        // -- LSB = 1/10 microdegree
        // -- Providing a range of plus-minus 180 degrees
        Double returnValue = null;
        if (j2735Long != 1800000001) {
            returnValue = j2735Long * 1e-7;
        }
        return returnValue;
    }

    public static Double convertLat(long j2735Lat) {
        // Latitude ::= INTEGER (-900000000..900000001)
        // -- LSB = 1/10 microdegree
        // -- Providing a range of plus-minus 90 degrees
        Double returnValue = null;
        if (j2735Lat != 900000001) {
            returnValue = j2735Lat * 1e-7;
        }
        return returnValue;
    }

    /**
     * Converts a J2735 elevation value to meters. Providing a range of -409.5 to + 6143.9 meters. The value -4096 shall
     * be used when Unknown is to be sent.
     *
     * @param j2735Elev J2735 elevation value.
     * @return Elevation in meters, or null if unavailable.
     */
    public static Double convertElevation(long j2735Elev) {
        Double returnValue = null;
        if (j2735Elev != -4096) {
            returnValue = j2735Elev * 1e-1;
        }
        return returnValue;
    }

    public static Double convertAccelLatLong(long accelLatLong) {
        // Acceleration ::= INTEGER (-2000..2001)
        // -- LSB units are 0.01 m/s^2
        // -- the value 2000 shall be used for values greater than 2000
        // -- the value -2000 shall be used for values less than -2000
        // -- a value of 2001 means the value is unavailable
        Double returnValue = null;
        if (accelLatLong < -2000) {
            returnValue = -20.0;
        } else if (accelLatLong > 2001) {
            returnValue = 20.0;
        } else if (accelLatLong != 2001) {
            returnValue = accelLatLong * 0.01;
        }
        return returnValue;
    }

    public static Double convertAccelVert(long accelVert) {
        // VerticalAcceleration ::= INTEGER (-127..127)
        // -- LSB units of 0.02 G steps over -2.52 to +2.54 G
        // -- The value +127 shall be used for ranges >= 2.54 G
        // -- The value -126 shall be used for ranges <= -2.52 G
        // -- The value -127 shall be used for unavailable
        Double returnValue = null;
        if (accelVert != -127) {
            returnValue = accelVert * 0.02;
        }
        return returnValue;
    }

    public static Double convertAccelYaw(long accelYaw) {
        // YawRate ::= INTEGER (-32767..32767)
        // -- LSB units of 0.01 degrees per second (signed)
        Double returnValue = null;
        if (accelYaw >= -32767 && accelYaw <= 32767) {
            returnValue = accelYaw * 0.01;
        }
        return returnValue;
    }

    public static Double convertSemiMajor(long semiMajor) {
        // SemiMajorAxisAccuracy ::= INTEGER (0..255)
        // -- semi-major axis accuracy at one standard dev
        // -- range 0-12.7 meter, LSB = .05m
        // -- 254 = any value equal or greater than 12.70 meter
        // -- 255 = unavailable semi-major axis value
        Double returnValue = null;
        if (semiMajor >= 0 && semiMajor < 255) {
            returnValue = semiMajor * 0.05;
        }
        return returnValue;
    }

    public static Double convertSemiMinor(long semiMinor) {
        // SemiMinorAxisAccuracy ::= INTEGER (0..255)
        // -- semi-minor axis accuracy at one standard dev
        // -- range 0-12.7 meter, LSB = .05m
        // -- 254 = any value equal or greater than 12.70 meter
        // -- 255 = unavailable semi-minor axis val
        Double returnValue = null;
        if (semiMinor >= 0 && semiMinor < 255) {
            returnValue = semiMinor * 0.05;
        }
        return returnValue;
    }

    public static Double convertOrientation(long orientation) {
        // SemiMajorAxisOrientation ::= INTEGER (0..65535)
        // -- orientation of semi-major axis
        // -- relative to true north (0~359.9945078786 degrees)
        // -- LSB units of 360/65535 deg = 0.0054932479
        // -- a value of 0 shall be 0 degrees
        // -- a value of 1 shall be 0.0054932479 degrees
        // -- a value of 65534 shall be 359.9945078786 deg
        // -- a value of 65535 shall be used for orientation unavailable
        Double returnValue = null;
        if (orientation >= 0 && orientation < 65535) {
            returnValue = 0.0054932479 * orientation;
        }
        return returnValue;
    }

    public static Double convertAngle(long angle) {
        // SteeringWheelAngle ::= INTEGER (-126..127)
        // -- LSB units of 1.5 degrees, a range of -189 to +189 degrees
        // -- +001 = +1.5 deg
        // -- -126 = -189 deg and beyond
        // -- +126 = +189 deg and beyond
        // -- +127 to be used for unavai
        Double returnValue = null;
        if (angle >= -126 && angle < 127) {
            returnValue = angle * 1.5;
        }
        return returnValue;
    }

    public static Double convertHeading(long angle) {
        // Heading ::= INTEGER (0..28800)
        // -- LSB of 0.0125 degrees
        // -- A range of 0 to 359.9875 degrees
        Double returnValue = null;
        if (angle >= 0 && angle <= 28800) {
            returnValue = angle * 0.0125;
        }
        return returnValue;
    }

    public static Double convertLaneWidth(LaneWidth laneWidth) {
        if (laneWidth == null) { return null; }
        return laneWidth.getValue()*1e-2d;
    }

    public static Double convertSpeed(long speed) {
        // Speed ::= INTEGER (0..8191) -- Units of 0.02 m/s
        // -- The value 8191 indicates that
        // -- speed is unavailable
        Double returnValue = null;
        if (speed >= 0 && speed < 8191) {
            returnValue = speed * 0.02;
        }
        return returnValue;
    }

    public static Long convertDDateTime(List<String> validationMessages, DDateTime dDateTime) {
        if (dDateTime == null) {
            validationMessages.add("DDateTime is missing.");
            return null;
        }

        Integer year = convertDYear(dDateTime.getYear());
        if (year == null) {
            validationMessages.add("DDateTime 'year' field is missing.");
        }

        Integer month = convertDMonth(dDateTime.getMonth());
        if (month == null) {
            validationMessages.add("DDateTime 'month' field is missing.");
        }

        Integer dayOfMonth = convertDDay(dDateTime.getDay());
        if (dayOfMonth == null) {
            validationMessages.add("DDateTime 'day' field is missing.");
        }

        Integer hour = convertDHour(dDateTime.getHour());
        if (hour == null) {
            validationMessages.add("DDateTime 'hour' field is missing.");
        }

        Integer minute = convertDMinute(dDateTime.getMinute());
        if (minute == null) {
            validationMessages.add("DDateTime 'minute' field is missing.");
        }

        SecondNanos secondNanos = convertDSecond(dDateTime.getSecond());
        if (secondNanos == null) {
            validationMessages.add("DDateTime 'second' (millisecond of minute) field is missing.");
        }


        ZoneOffset offset = convertDOffset(dDateTime.getOffset());

        if (year != null && month != null && dayOfMonth != null && hour != null
                && minute != null && secondNanos != null) {
            OffsetDateTime odt = OffsetDateTime.of(year, month, dayOfMonth, hour, minute,
                    secondNanos.secondOfMinute(), secondNanos.nanoOfSecond(), offset);
            return odt.toInstant().toEpochMilli();
        }
        return null;
    }

    public static Integer convertDYear(DYear dYear) {
        if (dYear == null) return null;
        long value = dYear.getValue();
        // 0 represents unknown year
        if (value == 0) return null;
        return (int) value;
    }

    public static Integer convertDMonth(DMonth dMonth) {
        if (dMonth == null) return null;
        long value = dMonth.getValue();
        // 0 Represents unknown month
        if (value == 0) return null;
        return (int) value;
    }

    public static Integer convertDDay(DDay dDay) {
        if (dDay == null) return null;
        long value = dDay.getValue();
        // 0 represents unknown day
        if (value == 0) return null;
        return (int) value;
    }

    public static Integer convertDHour(DHour dHour) {
        if (dHour == null) return null;
        long value = dHour.getValue();
        // Per J2735 (2024) sec 7.34: 31 represents unknown hours and the values 24-30 are used by some applications
        // to represent schedule adherence.
        // But they are omitted here for use by the RTCM timestamp.
        if (value > 23) return null;
        return (int) value;
    }

    public static Integer convertDMinute(DMinute dMinute) {
        if (dMinute == null) return null;
        long value = dMinute.getValue();
        // Per J2735 (2024) sec 7.37: 60 represents unknown hours
        if (value == 60) return null;
        return (int) value;
    }

    /**
     * Millisecond of minute.
     *
     * @param dSecond DE_DSecond
     * @return second of minute, and nanosecond of second
     */
    public static SecondNanos convertDSecond(DSecond dSecond) {
        if (dSecond == null) return null;
        long value = dSecond.getValue();
        // Per J2735 (2024) sec. 7.43: 65535 represents unavailable, and values 61000 and above are reserved.
        if (value >= 61000) return null;
        final int secondOfMinute = Math.floorDiv((int) value, 1000);
        final int milliOfSecond = (int) value - (secondOfMinute * 1000);
        final int nanoOfSecond = milliOfSecond * 1000000;
        return new SecondNanos(secondOfMinute, nanoOfSecond);
    }

    public record SecondNanos(Integer secondOfMinute, Integer nanoOfSecond) {

    }


    /**
     * Convert time zone offset
     *
     * @param dOffset Offset in minutes
     * @return Java ZoneOffset
     */
    public static ZoneOffset convertDOffset(DOffset dOffset) {
        if (dOffset == null) return ZoneOffset.UTC;
        final int value = (int) dOffset.getValue();
        int offsetHours = Math.floorDiv(value, 60);
        int offsetMinutes = value - (offsetHours * 60);
        return ZoneOffset.ofHoursMinutes(offsetHours, offsetMinutes);
    }

    final static int MINUTES_PER_DAY = 24 * 60;

    /**
     * Produce a ZonedDateTime from a minute of the year, and ingest time, adjusting for the edge case where the
     * message is produced on New Year's Eve and ingested on New Year's Day.
     * @param minuteOfTheYear J2735 Minute-of-the-year Integer
     * @param ingestTime The ingest time
     * @return A ZonedDateTime for the minute of the year
     */
    public static ZonedDateTime convertMinuteOfYear(final MinuteOfTheYear minuteOfTheYear, final ZonedDateTime ingestTime) {
        if (minuteOfTheYear == null) return null;
        final int moy = (int)minuteOfTheYear.getValue();
        final int dayOfYear = (moy / MINUTES_PER_DAY) + 1;
        final boolean isLastDayOfYear = dayOfYear >= 365; // or second to last if leap year

        // J2735 (2024) - Section 7.109: Says DE_Minute of the year is the minute of the "current year in the time
        // system being used (typically UTC time)" so we assume it is in fact UTC time.
        final ZonedDateTime utcIngestTime = ingestTime.withZoneSameInstant(ZoneOffset.UTC);
        final int ingestDayOfYear = utcIngestTime.getDayOfYear();
        final boolean isIngestFirstDayOfYear = (ingestDayOfYear == 1);

        int year = utcIngestTime.getYear();
        // If message produced on last day of year, and ingested on first day of year, it was produced last year.
        if (isLastDayOfYear && isIngestFirstDayOfYear) {
            --year;
        }

        return convertMinuteOfYear(minuteOfTheYear, year);
    }

    /**
     * Convert Minute of Year to ZonedDateTime.
     * Requires knowing what year it is.
     * Value of 527040 represents "invalid"
     * @param minuteOfTheYear DE_MinuteOfYear
     * @param year The year
     * @return ZonedDateTime for the year at the beginning of the minute
     */
    public static ZonedDateTime convertMinuteOfYear(final MinuteOfTheYear minuteOfTheYear, final int year) { //
        if (minuteOfTheYear == null) return null;
        final long moy = minuteOfTheYear.getValue();
        if (moy == 527040L) return null;
        final String dateString = String.format("%d-01-01T00:00:00.00Z", year);
        final ZonedDateTime yearDate = Instant.parse(dateString).atZone(ZoneId.of("UTC"));
        return yearDate.plusMinutes(moy);
    }

    public static ZonedDateTime convertMinuteOfYearAndDSecond(final MinuteOfTheYear minuteOfTheYear,
                                                              final ZonedDateTime ingestTime, final DSecond dSecond) {
        ZonedDateTime minuteDate = convertMinuteOfYear(minuteOfTheYear, ingestTime);
        return convertMinuteOfYearAndDSecond(minuteDate, dSecond);
    }

    public static ZonedDateTime convertMinuteOfYearAndDSecond(final MinuteOfTheYear minuteOfTheYear, final int year, final DSecond dSecond) {
        ZonedDateTime minuteDate = convertMinuteOfYear(minuteOfTheYear, year);
        return convertMinuteOfYearAndDSecond(minuteDate, dSecond);
    }

    private static ZonedDateTime convertMinuteOfYearAndDSecond(final ZonedDateTime minuteDate, final DSecond dSecond) {
        if (minuteDate == null) return null;
        if (dSecond == null) return minuteDate;
        SecondNanos secondNanos = convertDSecond(dSecond);
        return minuteDate
                .withSecond(secondNanos.secondOfMinute())
                .withNano(secondNanos.nanoOfSecond());
    }



    public static Integer convertMsgCount(final MsgCount msgCount) {
        if (msgCount == null) return null;
        return (int)msgCount.getValue();
    }

    public static RegionIntersectionId convertIntersectionReferenceID(IntersectionReferenceID intersectionReferenceID) {
        if (intersectionReferenceID == null) return new RegionIntersectionId(null, null);
        var region = intersectionReferenceID.getRegion();
        Integer regionValue = region != null ? (int)region.getValue() : null;
        var id = intersectionReferenceID.getId();
        Integer idValue = id != null ? (int)id.getValue() : null;
        return new RegionIntersectionId(regionValue, idValue);
    }

    public record RegionIntersectionId(Integer region, Integer intersectionId) {
    }

    public static String convertVehicleID(final VehicleID vehicleID) {
        if (vehicleID == null) return null;
        // CHOICE of EntityID or StationID
        // EntityID is an Octet string, StationId is an integer
        // Return a String in either case
        if (vehicleID.getEntityID() != null) {
            return vehicleID.getEntityID().getValue();
        }
        if (vehicleID.getStationID() != null) {
            return Long.toString(vehicleID.getStationID().getValue());
        }
        return null;
    }

    public static AccessPointID convertIntersectionAccessPointID(final IntersectionAccessPoint iap) {
        if (iap == null) return new AccessPointID(null, null, null);

        // CHOICE of LaneID, ConnectionID, or ApproachID
        Integer laneId = null;
        Integer approachId = null;
        Integer connectionId = null;
        if (iap.getLane() != null) {
            laneId = (int)iap.getLane().getValue();
        }
        if (iap.getApproach() != null) {
            approachId = (int)iap.getApproach().getValue();
        }
        if (iap.getConnection() != null) {
            connectionId = (int)iap.getConnection().getValue();
        }
        return new AccessPointID(laneId, approachId, connectionId);
    }

    public record AccessPointID(Integer laneID, Integer approachID, Integer connectionID) {
    }

    public static ProcessedBasicVehicleRole convertBasicVehicleRole(final BasicVehicleRole role) {
        if (role != null && role.getName() != null) {
            return ProcessedBasicVehicleRole.fromName(role.getName());
        }
        return null;
    }

    public static ProcessedRequestSubRole convertRequestSubRole(final RequestSubRole role) {
        if (role != null && role.getName() != null) {
            return ProcessedRequestSubRole.fromName(role.getName());
        }
        return null;
    }

    public static ProcessedVehicleType convertVehicleType(final VehicleType vehicleType) {
        if (vehicleType != null && vehicleType.getName() != null) {
            return ProcessedVehicleType.fromName(vehicleType.getName());
        }
        return null;
    }

    public static ProcessedRequestImportanceLevel convertRequestImportanceLevel(final RequestImportanceLevel importanceLevel) {
        if (importanceLevel != null && importanceLevel.getName() != null) {
            return ProcessedRequestImportanceLevel.valueOf(importanceLevel.getName());
        }
        return null;
    }

    /**
     * DeltaTime ::= INTEGER (-122 .. 121)
     * -- Supporting a range of +/- 20 minute in steps of 10 seconds
     * -- the value of -121 shall be used when more than -20 minutes
     * -- the value of +120 shall be used when more than +20 minutes
     * -- the value -122 shall be used when the value is unavailable
     * @param deltaTime The difference from scheduled time
     * @return Duration in seconds
     */
    public static Duration convertDeltaTime(final DeltaTime deltaTime) {
        if (deltaTime == null) { return null; }
        long value = (int)deltaTime.getValue();
        if (value == -122L) { return null; }
        return Duration.ofSeconds(value * 10);
    }


}
