package fr.ul.m2sid.clustering_api.util;

import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.SimpleDateFormat;

@Service
public class Converter {
    public static Date convertDateToSQLFormat(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            java.util.Date utilDate = format.parse(date);
            Date sqlDate = new Date(utilDate.getTime());
            return sqlDate;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
