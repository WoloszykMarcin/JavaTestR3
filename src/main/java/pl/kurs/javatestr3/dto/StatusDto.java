package pl.kurs.javatestr3.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusDto {
    private String status;

    public StatusDto() {
    }

    public StatusDto(String status) {
        this.status = status;
    }
}
