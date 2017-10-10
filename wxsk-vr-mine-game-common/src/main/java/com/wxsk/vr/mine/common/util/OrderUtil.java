package com.wxsk.vr.mine.common.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.util.Enumeration;

public class OrderUtil {

    public static int seed = 0;
    private static String localIp = null;

    static {
        setLocalIp();
    }

    public static void setLocalIp() {
        try {
            localIp = getLocalIp();
            localIp = localIp.split("\\.")[3];
            String patternIp = "000";
            DecimalFormat ipFormat = new DecimalFormat(patternIp);
            localIp = ipFormat.format(Integer.valueOf(localIp));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static String getLocalIp() {
        StringBuilder ip = new StringBuilder();
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            && !inetAddress.isLinkLocalAddress()
                            && inetAddress.isSiteLocalAddress()) {
                        ip.append(inetAddress.getHostAddress().toString());
                    }

                }
            }
        } catch (SocketException ex) {
        }
        return ip.toString();
    }

    /**
     * 生成唯一订单号。
     *
     * @param pre
     * @return
     */
    public synchronized static String createOrderCode() {
        return createOrderCode(100);
    }

    /**
     * 生成唯一订单号。
     *
     * @param pre
     * @return
     */
    private synchronized static String createOrderCode(long preOrder) {
        String pattern = "00";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        String patternSeed = "0000";
        DecimalFormat seedFormat = new DecimalFormat(patternSeed);
        if (null == localIp || "".equals(localIp)) {
            setLocalIp();
        }
        return decimalFormat.format(preOrder) + System.currentTimeMillis() + localIp + seedFormat.format(getSeed());
    }

    private synchronized static int getSeed() {
        if (++seed > 9999) {
            seed = 1;
        }
        return seed;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 20000; i++) {
            System.out.println(createOrderCode(0));
        }
    }
}
