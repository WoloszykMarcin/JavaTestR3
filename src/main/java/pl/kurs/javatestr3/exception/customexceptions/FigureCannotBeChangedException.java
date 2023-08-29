package pl.kurs.javatestr3.exception.customexceptions;

import org.hibernate.StaleObjectStateException;

public class FigureCannotBeChangedException extends StaleObjectStateException {
    public FigureCannotBeChangedException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
