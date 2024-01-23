package com.sportsecho.product.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDto {

    private String title;
    private String content;
    private int price;
    private int quantity;
    private List<String> imageUrlList;

}
