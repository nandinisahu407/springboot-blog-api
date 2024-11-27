package com.example.blog.payload;

import com.example.blog.dto.PostDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class PostResponse {

    List<PostDto> content;
    int pageNumber;
    int pageSize;
    long totalElements;
    int totalPages;
    boolean lastPage;
}
