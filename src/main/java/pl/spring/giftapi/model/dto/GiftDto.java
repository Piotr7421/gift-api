package pl.spring.giftapi.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GiftDto {

    private int id;
    private String name;
    private double price;
}
