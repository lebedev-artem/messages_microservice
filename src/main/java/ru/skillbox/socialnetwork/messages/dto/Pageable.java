package ru.skillbox.socialnetwork.messages.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.data.domain.Sort;

@Data
@Schema
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Pageable {

    Integer page;

    Integer size;

    Sort sort;

}
