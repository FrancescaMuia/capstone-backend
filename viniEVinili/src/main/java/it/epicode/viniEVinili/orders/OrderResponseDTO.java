package it.epicode.viniEVinili.orders;

import lombok.Data;

import java.time.LocalDate;

@Data
public class OrderResponseDTO {

    private Long id;
    private Long userId;
    //private List<ProductResponseDTO> products;
    private LocalDate orderDate;
    private OrderStatus status;
    private double totalAmount;
}
