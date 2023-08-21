package ru.skillbox.socialnetwork.messages.dto;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public class Pageable {

    Integer page;

    Integer size;

    Sort sort;

}
