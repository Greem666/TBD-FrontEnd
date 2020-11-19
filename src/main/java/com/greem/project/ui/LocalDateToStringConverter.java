package com.greem.project.ui;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

import java.time.LocalDate;

public class LocalDateToStringConverter implements Converter<LocalDate, String> {
    @Override
    public Result<String> convertToModel(LocalDate value, ValueContext context) {
        try {
            String yearString = value.getDayOfMonth() + "-" + value.getMonthValue() + "-" + value.getYear();
            return Result.ok(yearString);
        } catch (Exception e) {
            return Result.error("Invalid date");
        }
    }

    @Override
    public LocalDate convertToPresentation(String value, ValueContext context) {
        int day = Integer.parseInt(value.split("-")[0]);
        int month = Integer.parseInt(value.split("-")[1]);
        int year = Integer.parseInt(value.split("-")[2]);
        return LocalDate.of(year, month, day);
    }
}
