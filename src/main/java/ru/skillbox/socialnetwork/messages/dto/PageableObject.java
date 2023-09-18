package ru.skillbox.socialnetwork.messages.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.data.domain.Sort;

@Data
@Schema
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageableObject {

    Sort sort;

    boolean unpaged;

    boolean paged;

    Integer pageSize;

    Integer pageNumber;

    Integer offset;

}
