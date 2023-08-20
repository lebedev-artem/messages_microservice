package dto;


import lombok.Getter;
import lombok.Setter;
import net.bytebuddy.TypeCache;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public class Pageable {

    Integer page;

    Integer size;

    Sort sort;

}
