package ru.skillbox.socialnetwork.messages.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.data.domain.Sort;

@Data
@Schema
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageDialogDto {

    Integer totalPages;

    Integer totalElements;

    Sort sort;

    Integer numberOfElements;

    PageableObject pagable;

    boolean first;

    boolean last;

    Integer size;

    DialogDto content;

    Integer number;

    boolean empty;


}
