package com.example.form;

import com.example.domain.Task;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class TaskForm {
    @NotNull
    @Size(max = 32)
    private String title;

    @Size(max = 255)
    private String description;

    @NotNull
    @Pattern(regexp = "OPEN|CLOSE|CANCEL")
    private String status;

    public Task.Status getStatusType() {
        return Task.Status.Companion.getStatus(this.status);
    }
}
