package dto;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

@Getter
@Setter

public class PageableObject {


    Sort sort;

    boolean unpaged;

    boolean paged;

    Integer pageSize;

    Integer pageNumber;

    Integer offset;

}
