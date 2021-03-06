/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dtstack.flinkx.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Date Utilities
 *
 * Company: www.dtstack.com
 * @author huyifan.zju@163.com
 */
public class DateUtil {

    static final String timeZone = "GMT+8";

    static final String datetimeFormat = "yyyy-MM-dd HH:mm:ss";

    static final String dateFormat = "yyyy-MM-dd";

    static final String timeFormat = "HH:mm:ss";

    static final String yearFormat = "yyyy";

    static TimeZone timeZoner;

    static SimpleDateFormat datetimeFormatter;

    static SimpleDateFormat dateFormatter;

    static SimpleDateFormat timeFormatter;

    static SimpleDateFormat yearFormatter;

    static String START_TIME = "1970-01-01";

    public final static String DATE_REGEX = "(?i)date";

    public final static String TIMESTAMP_REGEX = "(?i)timestamp";

    public final static String DATETIME_REGEX = "(?i)datetime";

    private DateUtil() {}


    public static java.sql.Date columnToDate(Object column,SimpleDateFormat customTimeFormat) {
        if(column == null) {
            return null;
        } else if(column instanceof String) {
            if (((String) column).length() == 0){
                return null;
            }
            return new java.sql.Date(stringToDate((String)column,customTimeFormat).getTime());
        } else if (column instanceof Integer) {
            Integer rawData = (Integer) column;
            return new java.sql.Date(getMillSecond(rawData.toString()));
        } else if (column instanceof Long) {
            Long rawData = (Long) column;
            return new java.sql.Date(getMillSecond(rawData.toString()));
        } else if (column instanceof java.sql.Date) {
            return (java.sql.Date) column;
        } else if(column instanceof Timestamp) {
            Timestamp ts = (Timestamp) column;
            return new java.sql.Date(ts.getTime());
        } else if(column instanceof Date) {
            Date d = (Date)column;
            return new java.sql.Date(d.getTime());
        }

        throw new IllegalArgumentException("Can't convert " + column.getClass().getName() + " to Date");
    }

    public static java.sql.Timestamp columnToTimestamp(Object column,SimpleDateFormat customTimeFormat) {
        if (column == null) {
            return null;
        } else if(column instanceof String) {
            if (((String) column).length() == 0){
                return null;
            }
            return new java.sql.Timestamp(stringToDate((String)column,customTimeFormat).getTime());
        } else if (column instanceof Integer) {
            Integer rawData = (Integer) column;
            return new java.sql.Timestamp(getMillSecond(rawData.toString()));
        } else if (column instanceof Long) {
            Long rawData = (Long) column;
            return new java.sql.Timestamp(getMillSecond(rawData.toString()));
        } else if (column instanceof java.sql.Date) {
            return (java.sql.Timestamp) column;
        } else if(column instanceof Timestamp) {
            return (Timestamp) column;
        } else if(column instanceof Date) {
            Date d = (Date)column;
            return new java.sql.Timestamp(d.getTime());
        }

        throw new IllegalArgumentException("Can't convert " + column.getClass().getName() + " to Date");
    }

    public static long getMillSecond(String data){
        long time  = Long.valueOf(data);
        if(data.length() == 10){
            time = Long.valueOf(data) * 1000;
        } else if(data.length() == 13){
            time = Long.valueOf(data);
        } else if(data.length() == 16){
            time = Long.valueOf(data) / 1000;
        } else if(data.length() == 19){
            time = Long.valueOf(data) / 1000000 ;
        } else if(data.length() < 10){
            try {
                long day = Long.valueOf(data);
                Date date = dateFormatter.parse(START_TIME);
                Calendar cal = Calendar.getInstance();
                long addMill = date.getTime() + day * 24 * 3600 * 1000;
                cal.setTimeInMillis(addMill);
                time = cal.getTimeInMillis();
            } catch (Exception ignore){
            }
        }
        return time;
    }

    public static Date stringToDate(String strDate,SimpleDateFormat customTimeFormat)  {
        if(strDate == null || strDate.trim().length() == 0) {
            return null;
        }

        if(customTimeFormat != null){
            try {
                return customTimeFormat.parse(strDate);
            } catch (ParseException ignored) {
            }
        }

        try {
            return datetimeFormatter.parse(strDate);
        } catch (ParseException ignored) {
        }

        try {
            return dateFormatter.parse(strDate);
        } catch (ParseException ignored) {
        }

        try {
            return timeFormatter.parse(strDate);
        } catch (ParseException ignored) {
        }

        try {
            return yearFormatter.parse(strDate);
        } catch (ParseException ignored) {
        }

        throw new RuntimeException("can't parse date");
    }

    public static String dateToString(Date date) {
        return dateFormatter.format(date);
    }

    public static String timestampToString(Date date) {
        return datetimeFormatter.format(date);
    }

    public static String dateToYearString(Date date) {
        return yearFormatter.format(date);
    }

    public static SimpleDateFormat getDateFormatter(String timeFormat){
        SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
        sdf.setTimeZone(timeZoner);
        return sdf;
    }

    static {
        timeZoner = TimeZone.getTimeZone(timeZone);

        datetimeFormatter = new SimpleDateFormat(datetimeFormat);
        datetimeFormatter.setTimeZone(timeZoner);

        dateFormatter = new SimpleDateFormat(dateFormat);
        dateFormatter.setTimeZone(timeZoner);

        timeFormatter =  new SimpleDateFormat(timeFormat);
        timeFormatter.setTimeZone(timeZoner);

        yearFormatter = new SimpleDateFormat(yearFormat);
        yearFormatter.setTimeZone(timeZoner);
    }

}
