package dto;

import dto.MessageShortDto;
import dto.PageableObject;
import lombok.Getter;
import lombok.Setter;
import org.springdoc.core.converters.models.Sort;

@Getter
@Setter

public class PageMessageShortDto {

    Integer totalPages;

    Integer totalElements;

    Sort sort;

    Integer numberOfElements;

    PageableObject pageable;

    boolean first;

    boolean last;

    Integer size;

    MessageShortDto content;

    Integer number;

    boolean empty;



}
