package com.zhuanglide.util;

/**
 * Created by wwj on 16.1.26.
 */
public class Utils {

    /**
     * ip字符串转long
     * @param ip
     * @return
     */
    public static long ipv4String2long(String ip) {
        String[] iparry = ip.split("\\.");
        if (iparry.length != 4) {
            throw new IllegalArgumentException();
        }
        long ipl = 0l;
        for(int i=0;i<iparry.length;i++) {
            ipl += Long.valueOf(iparry[i]);
            if (i < iparry.length - 1) {
                ipl = ipl << 8;
            }
        }
        return ipl;
    }

    /**
     * ip long转string
     * @param ipl
     * @return
     */
    public static String ipv4Long2String(long ipl) {
        String[] iparray = new String[4];
        for(int i=0;i<iparray.length;i++) {
            iparray[i] = String.valueOf(ipl % 256);
            ipl = ipl >> 8;
        }
        String ip = "";
        for(int i=iparray.length-1;i>=0;i--) {
            ip += iparray[i];
            if (i > 0) {
                ip += ".";
            }
        }
        return ip;
    }
}
