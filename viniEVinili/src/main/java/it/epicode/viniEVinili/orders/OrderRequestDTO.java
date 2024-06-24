package it.epicode.viniEVinili.orders;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class OrderRequestDTO {

    private Long userId;
    private List<Long> productIds;
    private LocalDate orderDate;
    private OrderStatus status;
    private double totalAmount;
}
