package com.example.order.service;

import com.example.inventory.dto.InventoryDTO;
import com.example.order.common.ErrorGoodResponse;
import com.example.order.common.GoodResponse;
import com.example.order.common.SuccessGoodResponse;
import com.example.order.dto.GoodDTO;
import com.example.order.model.Good;
import com.example.order.repo.GoodRepo;
import com.example.product.dto.ProductDTO;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Transactional
public class GoodService {
    private final WebClient inventoryWebClient;
    private final WebClient productWebClient;
    private final GoodRepo goodRepo;
    private final ModelMapper modelMapper;

    @Autowired
    public GoodService(WebClient inventoryWebClient, WebClient productWebClient, GoodRepo goodRepo, ModelMapper modelMapper) {
        this.inventoryWebClient = inventoryWebClient;
        this.productWebClient = productWebClient;
        this.goodRepo = goodRepo;
        this.modelMapper = modelMapper;
    }

    // Get all orders
    public List<GoodDTO> getAllOrders(){
        List<Good> orderList = goodRepo.findAll();
        return modelMapper.map(orderList, new TypeToken<List<GoodDTO>>(){}.getType());
    }

    // Get order by ID
// Get order by ID
    public GoodResponse getGoodById(int id) {
        try {
            return goodRepo.findById(id)
                    .<GoodResponse>map(good -> {
                        GoodDTO goodDTO = modelMapper.map(good, GoodDTO.class);
                        return new SuccessGoodResponse("Order fetched successfully!", goodDTO);
                    })
                    .orElseGet(() -> new ErrorGoodResponse("Order not found with id: " + id));
        } catch (Exception e) {
            return new ErrorGoodResponse("Error fetching order: " + e.getMessage());
        }
    }


    // Save order - UPDATED with proper error handling and availability check
    public GoodResponse saveOrder(GoodDTO goodDTO){
        Integer itemId = goodDTO.getItemId();
        Integer requestedQuantity = goodDTO.getAmount();

        try {
            InventoryDTO inventoryResponse = inventoryWebClient.get()
                    .uri("/api/v1/item/{itemId}", itemId)
                    .retrieve()
                    .onStatus(HttpStatus.NOT_FOUND::equals, response ->
                            Mono.error(new RuntimeException("Item not found with id: " + itemId))
                    )
                    .bodyToMono(InventoryDTO.class)
                    .block();

            if (inventoryResponse == null) {
                return new ErrorGoodResponse("Inventory service returned null response");
            }

            // Check availability status (1 = available, 0 = unavailable)
            if (inventoryResponse.getAvailability() != null && inventoryResponse.getAvailability() == 0) {
                return new ErrorGoodResponse("Sorry! Item is currently unavailable");
            }

            // Check quantity
            if (inventoryResponse.getQuantity() == null || inventoryResponse.getQuantity() <= 0) {
                return new ErrorGoodResponse("Sorry! Item is out of stock");
            }

            if (inventoryResponse.getQuantity() < requestedQuantity) {
                return new ErrorGoodResponse("Sorry! Only " + inventoryResponse.getQuantity() +
                        " items available. Requested: " + requestedQuantity);
            }

            // If all checks pass, save the order
            Good good = modelMapper.map(goodDTO, Good.class);
            Good savedOrder = goodRepo.save(good);
            GoodDTO savedOrderDTO = modelMapper.map(savedOrder, GoodDTO.class);

            return new SuccessGoodResponse("Order placed successfully!", savedOrderDTO);

        } catch (Exception e) {
            e.printStackTrace();
            return new ErrorGoodResponse("Error communicating with inventory service: " + e.getMessage());
        }
    }

    // Update order
    public GoodResponse updateOrder(GoodDTO goodDTO){
        try {
            Good good = modelMapper.map(goodDTO, Good.class);
            Good updatedOrder = goodRepo.save(good);
            GoodDTO updatedOrderDTO = modelMapper.map(updatedOrder, GoodDTO.class);
            return new SuccessGoodResponse("Order updated successfully!", updatedOrderDTO);
        } catch (Exception e) {
            return new ErrorGoodResponse("Error updating order: " + e.getMessage());
        }
    }

    // Delete order
    public GoodResponse deleteOrder(int id){
        if(goodRepo.existsById(id)){
            goodRepo.deleteById(id);
            return new SuccessGoodResponse("Order Deleted Successfully !!!");
        } else {
            return new ErrorGoodResponse("Order Not Found !!!");
        }
    }

    // Method to check item availability before ordering - CORRECTED
    public GoodResponse checkItemAvailability(Integer itemId, Integer quantity) {
        try {
            InventoryDTO inventoryResponse = inventoryWebClient.get()
                    .uri("/api/v1/item/{itemId}", itemId)
                    .retrieve()
                    .onStatus(HttpStatus.NOT_FOUND::equals, response ->
                            Mono.error(new RuntimeException("Item not found with id: " + itemId))
                    )
                    .bodyToMono(InventoryDTO.class)
                    .block();

            if (inventoryResponse == null) {
                return new ErrorGoodResponse("Item not found");
            }

            // Get product information using the correct productId from inventory
            Integer productId = inventoryResponse.getProductId();

            if (productId != null) {
                ProductDTO productResponse = productWebClient.get()
                        .uri("/api/v1/product/{productId}", productId) // Fixed: use productId, not itemId
                        .retrieve()
                        .onStatus(HttpStatus.NOT_FOUND::equals, response ->
                                Mono.error(new RuntimeException("Product not found with id: " + productId))
                        )
                        .bodyToMono(ProductDTO.class)
                        .block();

                if (productResponse == null) {
                    return new ErrorGoodResponse("Product information not available");
                }

                // Check if product is active
                if (!productResponse.isActive()) {
                    return new ErrorGoodResponse("Item is not for sale");
                }
            }

            // Check inventory availability
            if (inventoryResponse.getAvailability() != null && inventoryResponse.getAvailability() == 0) {
                return new ErrorGoodResponse("Item is currently unavailable");
            }

            if (inventoryResponse.getQuantity() == null || inventoryResponse.getQuantity() <= 0) {
                return new ErrorGoodResponse("Item is out of stock");
            }

            if (inventoryResponse.getQuantity() < quantity) {
                return new ErrorGoodResponse("Insufficient stock. Available: " +
                        inventoryResponse.getQuantity() + ", Requested: " + quantity);
            }

            return new SuccessGoodResponse("Item is available for order");

        } catch (Exception e) {
            return new ErrorGoodResponse("Error checking availability: " + e.getMessage());
        }
    }
}