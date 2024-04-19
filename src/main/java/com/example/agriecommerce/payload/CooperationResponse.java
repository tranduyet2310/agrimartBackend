package com.example.agriecommerce.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CooperationResponse {
    private List<CooperationDto> content;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPage;
    private boolean last;
}
