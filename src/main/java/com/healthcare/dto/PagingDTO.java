package com.healthcare.dto;

import lombok.Data;

public @Data class PagingDTO {

    private int page = 0;
    private int perpage = 20000;
    
    private int offset;

}
