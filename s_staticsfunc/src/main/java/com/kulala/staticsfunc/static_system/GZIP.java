package com.kulala.staticsfunc.static_system;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GZIP {
    public static String toGZIPString(String str) throws UnsupportedEncodingException, IOException {
        if (str == null || str.equals("")) return "";
        byte[]                byts = str.getBytes("UTF-8");
        ByteArrayOutputStream bos  = new ByteArrayOutputStream();
        GZIPOutputStream      gzip = new GZIPOutputStream(bos);
        gzip.write(byts);
        gzip.finish();
        gzip.close();
        byte[] byts1 = bos.toByteArray();
        bos.close();
        String str1 = new String(byts1, "ISO-8859-1");
        return str1;
    }

    public static String getGZIPString(byte[] byts) throws UnsupportedEncodingException, IOException {
        if (byts == null) return "";
        ByteArrayOutputStream out     = new ByteArrayOutputStream();
        ByteArrayInputStream  in      = new ByteArrayInputStream(byts);
        GZIPInputStream       gunzip  = new GZIPInputStream(in);//error this pos
        byte[] buffer = new byte[1024 * 64];
        int    n;
        while ((n = gunzip.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        String result = out.toString("UTF-8");
        if (out != null) out.close();
        if (in != null) in.close();
        return out.toString("UTF-8");
    }
//    public static String getGZIPString(byte[] byts) throws UnsupportedEncodingException, IOException {
//        if (byts == null) return "";
//        ByteArrayOutputStream out     = new ByteArrayOutputStream();
//        ByteArrayInputStream  in      = new ByteArrayInputStream(byts);
//        GZIPInputStream       gunzip  = new GZIPInputStream(in);//error this pos
//        byte[] buffer = new byte[1024 * 64];
//        int    n;
//        while ((n = gunzip.read(buffer)) != -1) {
//            out.write(buffer, 0, n);
//        }
//        String result = out.toString("UTF-8");
//        if (out != null) out.close();
//        if (in != null) in.close();
//        return out.toString("UTF-8");
//    }
}
