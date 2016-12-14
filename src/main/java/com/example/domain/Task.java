package com.example.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "tasks")
@Data
@AllArgsConstructor
public class Task {
    public class IllegalStatusException extends RuntimeException {
        public IllegalStatusException() {
            super();
        }

        public IllegalStatusException(String message) {
            super(message);
        }
    }

    public Task() {
        this.status = Status.OPEN;
    }

    @Id
    @GeneratedValue
    private Integer id;

    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private Status status;

    public void setStatusByString(String str) {
        Status status = Status.getStatus(str);
        if (status == null) {
            throw new IllegalStatusException();
        }

        this.status = status;
    }

    public enum Status {
        OPEN, CLOSE, CANCEL;

        private static final Map<String, Status> statusMap = new HashMap<>();

        static {
            for (Status status : Status.values()) {
                statusMap.put(status.name(), status);
            }
        }

        public static Status getStatus(String status) {
            return statusMap.get(status);
        }
    }
}
