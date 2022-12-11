package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class BookForm {

    private long id;
    @NotEmpty(message = "상품 이름은 필수입니다.")
    private String name;
    @Min(value = 1, message = "상품 가격은 0원 이상이여야 합니다.")
    private int price;

    private int stockQuantity;

    private String author;
    private String isbn;
}
