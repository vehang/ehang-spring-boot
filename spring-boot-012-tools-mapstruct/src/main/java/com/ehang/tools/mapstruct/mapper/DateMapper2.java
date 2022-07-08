package com.ehang.tools.mapstruct.mapper;

import org.mapstruct.Named;

import java.text.SimpleDateFormat;
import java.util.Date;

@Named("dateMapper2")
public class DateMapper2 {
    public String toString(Date date) {
        return date != null ? new SimpleDateFormat("yyyy/MM/dd").format(date) : null;
    }

    public Date toDate(String date) {
        try {
            return date != null ? new SimpleDateFormat("yyyy/MM/dd").parse(date) : null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
