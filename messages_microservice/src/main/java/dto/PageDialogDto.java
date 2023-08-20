package dto;


import lombok.Getter;
import lombok.Setter;
import org.springdoc.core.converters.models.Sort;

@Getter
@Setter

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
