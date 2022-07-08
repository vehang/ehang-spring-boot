package com.ehang.tools.mapstruct.mapper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateMapper {
    public String toString(Date date) {
        return date != null ? new SimpleDateFormat("yyyy-MM-dd").format(date) : null;
    }

    public Date toDate(String date) {
        try {
            return date != null ? new SimpleDateFormat("yyyy-MM-dd").parse(date) : null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
