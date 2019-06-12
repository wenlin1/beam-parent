package com.hsshy.beam.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class IdUtils {

    public static String getIdNumber() {
        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase().replaceAll("[^\\d]", "");
        ;
        return uuid;
    }

    public static String getId() {
        String orderNo = "";
        String trandNo = String.valueOf((Math.random() * 9 + 1) * 1000000);
        String sdf = new SimpleDateFormat("HHMMSS").format(new Date());
        orderNo = trandNo.toString().substring(0, 3);
        orderNo = orderNo + sdf;
        return orderNo;
    }
}
