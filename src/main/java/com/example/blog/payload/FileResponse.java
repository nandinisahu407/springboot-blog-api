package com.example.blog.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class FileResponse {
    String fileName;
    String message;

    public FileResponse(String fileName, String message) {
        this.fileName = fileName;
        this.message = message;
    }
}
