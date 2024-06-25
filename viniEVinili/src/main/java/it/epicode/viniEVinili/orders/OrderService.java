package it.epicode.viniEVinili.orders;

import it.epicode.viniEVinili.products.Product;
import it.epicode.viniEVinili.products.ProductRepository;
import it.epicode.viniEVinili.users.User;
import it.epicode.viniEVinili.users.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }

    public Order findById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));
    }

    /*public Order save(Order order) {
        return orderRepository.save(order);
    }

     */
    public OrderResponseDTO save(OrderRequestDTO orderRequestDTO) {
        User user = userRepository.findById(orderRequestDTO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + orderRequestDTO.getUserId()));

        List<Product> products = productRepository.findAllById(orderRequestDTO.getProductIds());
        if (products.size() != orderRequestDTO.getProductIds().size()) {
            throw new EntityNotFoundException("One or more products not found");
        }

        Order order = new Order();
        BeanUtils.copyProperties(orderRequestDTO, order);
        order.setUser(user);
        order.setProducts(products);

        Order savedOrder = orderRepository.save(order);

        OrderResponseDTO response = new OrderResponseDTO();
        BeanUtils.copyProperties(savedOrder, response);
        response.setUserId(savedOrder.getUser().getId());
        return response;
    }

    /*public Order update(Order order) {
        // Controlla se l'ordine esiste
        if (!orderRepository.existsById(order.getId())) {
            throw new EntityNotFoundException("Cannot update order. Order with id " + order.getId() + " not found.");
        }
        return orderRepository.save(order);
    }

     */
    public OrderResponseDTO update(Long id, OrderRequestDTO orderRequestDTO) {
        if (!orderRepository.existsById(id)) {
            throw new EntityNotFoundException("Order not found with id: " + id);
        }

        Order order = orderRepository.findById(id).get();
        User user = userRepository.findById(orderRequestDTO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + orderRequestDTO.getUserId()));

        List<Product> products = productRepository.findAllById(orderRequestDTO.getProductIds());
        if (products.size() != orderRequestDTO.getProductIds().size()) {
            throw new EntityNotFoundException("One or more products not found");
        }

        BeanUtils.copyProperties(orderRequestDTO, order);
        order.setUser(user);
        order.setProducts(products);

        Order updatedOrder = orderRepository.save(order);

        OrderResponseDTO response = new OrderResponseDTO();
        BeanUtils.copyProperties(updatedOrder, response);
        response.setUserId(updatedOrder.getUser().getId());
        return response;
    }

    public void delete(Long orderId) {
        orderRepository.deleteById(orderId);
    }
}
