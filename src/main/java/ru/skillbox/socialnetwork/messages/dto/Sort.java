package ru.skillbox.socialnetwork.messages.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Sort {

    boolean sorted;
    boolean unsorted;
    boolean empty;
}
