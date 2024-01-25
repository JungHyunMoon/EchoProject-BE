package com.sportsecho.memberProduct.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberProductResponseDto {

    private Long id;

    private int productsQuantity;

    private String title;

    private int price;

    private List<String> imageUrlList;
}
