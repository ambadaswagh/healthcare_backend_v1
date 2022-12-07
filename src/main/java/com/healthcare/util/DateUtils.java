package com.healthcare.util;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 * Immutable Class for some date utils methods
 *
 * @author Anass
 *
 */
public final class DateUtils {

	public static final String DAY_FORMAT = "EEEE";

	public static final String US_LOCAL = "US";

	public static final String LOCAL_TIMEZONE = "UTC";
	/**
	 * date format yyyy-MM-dd
	 */
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final String TIME_FORMAT = "HH:mm:ss";
	public static final String TIME_FORMAT2 = "HH:mm";
	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	/**
	 * days delimiter
	 */
	public static final String DAYS_DELIMITER = ",";

	/**
	 * constructor
	 */
	private DateUtils() {
	};

	public static Map<String, String> get_map_timezones(){
		List<String> zoneList = Arrays.asList(TimeZone.getAvailableIDs());
		Map<String, String> map_timezones = new HashMap<>();
		LocalDateTime dt = LocalDateTime.now(TimeZone.getTimeZone("UTC").toZoneId());
		for (String zoneId : zoneList) {
			ZoneId zone = TimeZone.getTimeZone(zoneId).toZoneId();

            ZonedDateTime zdt = dt.atZone(zone);
            ZoneOffset zos = zdt.getOffset();

            //replace Z to +00:00
            String offset = zos.getId().replaceAll("Z", "+00:00");

            //result.put(zone.toString(), offset);
            String map_key = "UTC" + offset;

            if(!map_timezones.containsKey(map_key)){
            	map_timezones.put(map_key, zoneId);
            }
        }
		return map_timezones;
	}

	public static String get_timezone(String string_that_contains_timezone){
		if(string_that_contains_timezone.contains(LOCAL_TIMEZONE)){
			int index = string_that_contains_timezone.indexOf(LOCAL_TIMEZONE);
			String timezone = string_that_contains_timezone.substring(index);
			if(!timezone.contains(":")){
				timezone += ":00";
			}
			if(timezone.contains("−"))
				timezone = timezone.replace("−", "-");
			System.err.println(timezone);
			timezone = DateUtils.get_map_timezones().get(timezone.trim().toString());
			System.err.println(timezone);
			return timezone;
		}
		return LOCAL_TIMEZONE;
	}

	public static Timestamp get_local_time(Timestamp timestamp, String target_time_zone){
		DateTime dateTimeWithTimeZone = new DateTime(DateTimeZone.forID(LOCAL_TIMEZONE)).withMillis(timestamp.getTime());
		Date dateWithTimeZoneIncluded = dateTimeWithTimeZone.toDate();
		try {
			DateFormat pstFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
			DateFormat pstFormat2 = new SimpleDateFormat(DATE_TIME_FORMAT);
				pstFormat.setTimeZone(TimeZone.getTimeZone(target_time_zone));
			String pst_date_string = pstFormat.format(dateWithTimeZoneIncluded);
			Date new_date = pstFormat2.parse(pst_date_string);
			return new Timestamp(new_date.getTime());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return timestamp;
		}
	}

	public static Timestamp get_local_timestamp_time(Timestamp timestamp, String target_time_zone){
		DateTime dateTimeWithTimeZone = new DateTime(DateTimeZone.forID(LOCAL_TIMEZONE)).withMillis(timestamp.getTime());
		Date dateWithTimeZoneIncluded = dateTimeWithTimeZone.toDate();
		try {
			DateFormat pstFormat = new SimpleDateFormat(TIME_FORMAT);
			DateFormat pstFormat2 = new SimpleDateFormat(TIME_FORMAT);
			pstFormat.setTimeZone(TimeZone.getTimeZone(target_time_zone));
			String pst_date_string = pstFormat.format(dateWithTimeZoneIncluded);
			Date new_date = pstFormat2.parse(pst_date_string);
			return new Timestamp(new_date.getTime());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return timestamp;
		}
	}

	public static Date get_local_time(Date date, String target_time_zone){
		DateTime dateTimeWithTimeZone = new DateTime(DateTimeZone.forID(LOCAL_TIMEZONE)).withMillis(date.getTime());
		Date dateWithTimeZoneIncluded = dateTimeWithTimeZone.toDate();
		try {
			DateFormat pstFormat = new SimpleDateFormat(DATE_FORMAT);
			DateFormat pstFormat2 = new SimpleDateFormat(DATE_FORMAT);
			pstFormat.setTimeZone(TimeZone.getTimeZone(target_time_zone));
			String pst_date_string = pstFormat.format(dateWithTimeZoneIncluded);
			Date new_date = pstFormat2.parse(pst_date_string);
			return new_date;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return date;
		}
	}

	public static Time get_local_time(Time time, String target_time_zone){
		DateTime dateTimeWithTimeZone = new DateTime(DateTimeZone.forID(LOCAL_TIMEZONE)).withMillis(time.getTime());
		Date dateWithTimeZoneIncluded = dateTimeWithTimeZone.toDate();
		try {
			DateFormat pstFormat = new SimpleDateFormat(TIME_FORMAT);
			DateFormat pstFormat2 = new SimpleDateFormat(TIME_FORMAT);
			pstFormat.setTimeZone(TimeZone.getTimeZone(target_time_zone));
			String pst_date_string = pstFormat.format(dateWithTimeZoneIncluded);
			Date new_date = pstFormat2.parse(pst_date_string);
			return new Time(new_date.getTime());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return time;
		}
	}

	public static String get_local_time_string(String timeEnd, String target_time_zone) {
		try {
			DateFormat utcFormat = new SimpleDateFormat(TIME_FORMAT2);
			DateTime dateTimeWithTimeZone = new DateTime(DateTimeZone.forID(LOCAL_TIMEZONE)).withMillis(utcFormat.parse(timeEnd).getTime());
			Date dateWithTimeZoneIncluded = dateTimeWithTimeZone.toDate();
			System.err.println(dateWithTimeZoneIncluded.toString());
			DateFormat pstFormat = new SimpleDateFormat(TIME_FORMAT2);
			pstFormat.setTimeZone(TimeZone.getTimeZone(target_time_zone));
			String pst_date_string = pstFormat.format(dateWithTimeZoneIncluded);
			return pst_date_string;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return timeEnd;
		}
	}

	public static Date get_local_date_time(Date date, String target_time_zone) {
		DateTime dateTimeWithTimeZone = new DateTime(DateTimeZone.forID(LOCAL_TIMEZONE)).withMillis(date.getTime());
		Date dateWithTimeZoneIncluded = dateTimeWithTimeZone.toDate();
		try {
			DateFormat pstFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
			DateFormat pstFormat2 = new SimpleDateFormat(DATE_TIME_FORMAT);
			pstFormat.setTimeZone(TimeZone.getTimeZone(target_time_zone));
			String pst_date_string = pstFormat.format(dateWithTimeZoneIncluded);
			Date new_date = pstFormat2.parse(pst_date_string);
			return new_date;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return date;
		}
	}

	/**
	 * format date to String
	 *
	 * @param date
	 * @param format
	 * @return String
	 */
	public static String formatString(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	/**
	 * generate service calendar
	 *
	 * @param startdate
	 * @param enddate
	 * @return List<Date>
	 */
	public static List<Date> getDaysBetweenDates(Date startdate, Date enddate, String SchudeledDays) {
		List<Date> dates = new ArrayList<Date>();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(startdate);
		String[] splitedSchudeledDays = SchudeledDays.split(DAYS_DELIMITER);
		List<String> scheduledTableDates = Arrays.asList(splitedSchudeledDays);
		while (calendar.getTime().before(enddate)) {
			Date result = calendar.getTime();
			if (scheduledTableDates
					.contains(new SimpleDateFormat(DAY_FORMAT, new Locale(US_LOCAL)).format(result).toUpperCase()))
				dates.add(result);
			calendar.add(Calendar.DATE, 1);
		}
		return dates;
	}

	public static String getCurrentDate(String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(new Date());
	}

	public static Date stringToDate(String format, String date) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			return null;
		}
	}

	public static String dateToString(String format, Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	/**
	 * convert timeStamp to Date
	 *
	 * @param timestamp
	 * @return
	 */
	public static Date timestampToDate(Timestamp timestamp) {
		if (timestamp != null) {
			Date date = new Date();
			date.setTime(timestamp.getTime());
			return date;
		}
		return null;
	}

	/**
	 * convert date to Calendar
	 *
	 * @param date
	 * @return
	 */
	public static Calendar dateToCalendar(Date date) {
		if (date != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			return cal;
		}
		return null;
	}

	/**
	 * checks if a given in date intervall
	 *
	 * @param givenDate
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static Boolean isAgivenDateBetweenInterval(Calendar givenDate, Calendar startDate, Calendar endDate) {
		if (givenDate != null && startDate != null && endDate != null) {
			return givenDate.after(startDate) && givenDate.before(endDate);
		}
		return null;
	}

	public static Date getLastDayOfYear(Integer year) {
		Calendar calendar = new GregorianCalendar(year, 12, Calendar.DAY_OF_MONTH);
		calendar.set(Calendar.MONTH, calendar.getActualMaximum(Calendar.MONTH));
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
		calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));
		return calendar.getTime();
	}

	public static Date getFirstDayOfYear(Integer year) {
		Calendar calendar = new GregorianCalendar(year, 1, Calendar.DAY_OF_MONTH);
		calendar.set(Calendar.MONTH, calendar.getActualMinimum(Calendar.MONTH));
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
		calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
		return calendar.getTime();
	}

	public static Date getFirstDayOfCurrentWeek() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
		cal.set(Calendar.HOUR_OF_DAY, cal.getActualMinimum(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, cal.getActualMinimum(Calendar.MINUTE));
		cal.set(Calendar.SECOND, cal.getActualMinimum(Calendar.SECOND));
		return cal.getTime();
	}

	public static Date getLastDayOfCurrentWeek() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
		cal.add(Calendar.DAY_OF_MONTH, 7);
		cal.set(Calendar.HOUR_OF_DAY, cal.getActualMaximum(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, cal.getActualMaximum(Calendar.MINUTE));
		cal.set(Calendar.SECOND, cal.getActualMaximum(Calendar.SECOND));
		return cal.getTime();
	}

  /**
   *  It is used to get future date from current date
   * @param day
   * @return Date
   */
  public static Date getFutureDayBetweenInterval(int day) {
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    Calendar c = Calendar.getInstance();
    c.setTime(timestamp);
    c.add(Calendar.DATE, day);
    return c.getTime();
  }

  /**
   * @param date
   * @return String values of month and and like (MM-DD)
   */
  public static String getMonthAndDateFromDate(Date date){
     SimpleDateFormat formatter=new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
      Calendar cal = Calendar.getInstance();
      cal.setTime(date);
      int year = cal.get(Calendar.YEAR);
      cal.add(Calendar.MONTH,1);
      int month = cal.get(Calendar.MONTH);
      int day = cal.get(Calendar.DAY_OF_MONTH);
      return String.format("%02d",month) +"-" + String.format("%02d",day);

  }

}
