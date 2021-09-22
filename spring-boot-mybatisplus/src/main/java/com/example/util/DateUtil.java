package com.example.util;

import com.example.exception.BizException;
import com.example.base.ResultEnum;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtil {

    private static DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static DateTimeFormatter DATETIME_STR_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
    private static DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("H:m");

    private DateUtil() {
    }

    public static String date2String(Date date) {
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return DATETIME_FORMATTER.format(localDateTime);
    }

    public static Data string2Time(String time) {
        if (!StringUtils.hasText(time)) {
            throw new RuntimeException("时间不能为空！");
        }
        time = time.trim();
        LocalTime localTime = LocalTime.parse(time, TIME_FORMATTER);
        Data data = Data.build();
        data.setHour(localTime.getHour());
        data.setMinute(localTime.getMinute());
        return data;
    }

    public static DataRange string2Time(String startTime, String endTime) throws BizException {
        Data start = string2Time(startTime);
        Data end = string2Time(endTime);
        if (start.getHour().compareTo(end.getHour()) >= 0 && start.getMinute().compareTo(end.getMinute()) >= 0) {
            throw new BizException(ResultEnum.DATE_VALID_ERROR);
        }
        DataRange range = DataRange.build();
        range.setStartHour(start.getHour());
        range.setStartMinute(start.getMinute());
        range.setEndHour(end.getHour());
        range.setEndMinute(end.getMinute());
        return range;
    }

    public static String date2Str() {
        return LocalDateTime.now().format(DATETIME_STR_FORMATTER);
    }

    public static class Data {
        private Integer hour;
        private Integer minute;

        public Integer getHour() {
            return hour;
        }

        public void setHour(Integer hour) {
            this.hour = hour;
        }

        public Integer getMinute() {
            return minute;
        }

        public void setMinute(Integer minute) {
            this.minute = minute;
        }

        public static Data build() {
            return new Data();
        }
    }

    public static class DataRange {
        private Integer startHour;
        private Integer endHour;
        private Integer startMinute;
        private Integer endMinute;

        public Integer getStartHour() {
            return startHour;
        }

        public void setStartHour(Integer startHour) {
            this.startHour = startHour;
        }

        public Integer getEndHour() {
            return endHour;
        }

        public void setEndHour(Integer endHour) {
            this.endHour = endHour;
        }

        public Integer getStartMinute() {
            return startMinute;
        }

        public void setStartMinute(Integer startMinute) {
            this.startMinute = startMinute;
        }

        public Integer getEndMinute() {
            return endMinute;
        }

        public void setEndMinute(Integer endMinute) {
            this.endMinute = endMinute;
        }

        public static DataRange build() {
            return new DataRange();
        }

        @Override
        public String toString() {
            return "DataRange{" +
                    "startHour=" + startHour +
                    ", endHour=" + endHour +
                    ", startMinute=" + startMinute +
                    ", endMinute=" + endMinute +
                    '}';
        }
    }
}
