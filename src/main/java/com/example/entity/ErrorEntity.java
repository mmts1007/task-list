package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorEntity {
    private HttpStatus status;
    private List<String> details = new ArrayList<>();

    public int getStatus() {
        return status.value();
    }

    public String getMessage() {
        return status.getReasonPhrase();
    }

    public void addDetail(String message) {
        details.add(message);
    }
}
