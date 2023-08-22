package pl.kurs.javatestr3.service;

import org.springframework.stereotype.Service;
import pl.kurs.javatestr3.exception.customexceptions.InvalidDateFormatException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
public class DataUtil {
    public static LocalDateTime parseDate(String dateString) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            return LocalDateTime.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            throw new InvalidDateFormatException("Invalid date format");
        }
    }
}
