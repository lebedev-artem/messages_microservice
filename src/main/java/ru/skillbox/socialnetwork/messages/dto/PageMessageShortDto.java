package ru.skillbox.socialnetwork.messages.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

@Data
@Schema
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageMessageShortDto {

    Integer totalPages;

    Integer totalElements;

    Sort sort;

    Integer numberOfElements;

    PageableObject pageable;

    boolean first;

    boolean last;

    Integer size;

    List<MessageShortDto> content;

    Integer number;

    boolean empty;



}
