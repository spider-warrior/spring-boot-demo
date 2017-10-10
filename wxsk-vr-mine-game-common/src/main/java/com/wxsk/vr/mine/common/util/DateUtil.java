package com.wxsk.vr.mine.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    private static final SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");

    public static synchronized String yyyyMMddHHmmssFormat(Date date) {
        if (date == null) {
            return "";
        }
        return yyyyMMddHHmmss.format(date);
    }
    public static synchronized String yyyyMMddFormat(Date date) {
        if (date == null) {
            return "";
        }
        return yyyyMMdd.format(date);
    }

    public static synchronized Date yyyyMMddHHmmssParse(String date) throws ParseException {
        if (date == null) {
            return null;
        }
        return yyyyMMddHHmmss.parse(date);
    }
    public static synchronized Date yyyyMMddParse(String date) throws ParseException {
        if (date == null) {
            return null;
        }
        return yyyyMMdd.parse(date);
    }


    /**
     * 获取时长
     *
     * @param millisecond 毫秒数
     * @return
     */
    public static String getDistance(Long millisecond) {
        if (millisecond == null || millisecond < TimeNameEnum.秒.millisecond) {
            return "0秒";
        }
        long ds = millisecond / TimeNameEnum.天.millisecond;
        long hs = (millisecond % TimeNameEnum.天.millisecond) / TimeNameEnum.时.millisecond;
        long ms = (millisecond % TimeNameEnum.时.millisecond) / TimeNameEnum.分.millisecond;
        long ss = (millisecond % TimeNameEnum.分.millisecond) / TimeNameEnum.秒.millisecond;
        StringBuilder timeStr = new StringBuilder();
        if (ds != 0) {
            timeStr.append(ds).append(TimeNameEnum.天.name());
        }
        if (hs != 0 || ds > 0) {
            timeStr.append(hs).append(TimeNameEnum.时.name());
        }
        if (ms != 0 || ds != 0 || hs != 0) {
            timeStr.append(ms).append(TimeNameEnum.分.name());
        }
        if (ms != 0 || ds != 0 || hs != 0 || ss != 0) {
            timeStr.append(ss).append(TimeNameEnum.秒.name());
        }
        return timeStr.toString();
    }

    private static enum TimeNameEnum {
        天(1000 * 60 * 60 * 24L), 时(1000 * 60 * 60L), 分(1000 * 60L), 秒(1000L);
        private Long millisecond;

        TimeNameEnum(Long millisecond) {
            this.millisecond = millisecond;
        }
    }


    public static void main(String[] args) {
        long distance = 10000000001L;
        System.out.println(getDistance(distance));
    }


}
