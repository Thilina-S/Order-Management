package com.example.order.controller;

import com.example.base.dto.GoodEventDTO;
import com.example.order.common.GoodResponse;
import com.example.order.dto.GoodDTO;
import com.example.order.kafka.GoodProducer;
import com.example.order.service.GoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "api/v1")
public class GoodController {
    @Autowired
    private GoodService goodService;

    @Autowired
    private GoodProducer goodProducer;

    // Get all orders
    @GetMapping("/getorders")
    public List<GoodDTO> getOrders(){
        return goodService.getAllOrders();
    }

    // Get order by ID
    @GetMapping("/getorder/{id}")
    public GoodResponse getOrderById(@PathVariable int id) {
        return goodService.getGoodById(id);
    }


    // Save Order using the kakfa producer
    @PostMapping("/addorder")
    public GoodResponse saveOrder(@RequestBody GoodDTO goodDTO){
        GoodEventDTO goodEventDTO = new GoodEventDTO();
        goodEventDTO.setMessage("Order is Committed !"); //message filling
        goodEventDTO.setStatus("pending");
        goodProducer.sendMessage(goodEventDTO); //send the message

        return goodService.saveOrder(goodDTO); //fill the order
    }

    // Update Order
    @PutMapping("/updateorder")
    public GoodResponse updateOrder(@RequestBody GoodDTO goodDTO){
        return goodService.updateOrder(goodDTO);
    }

    // Delete order - changed return type to GoodResponse
    @DeleteMapping("/deleteorder/{id}")
    public GoodResponse deleteOrder(@PathVariable int id){
        return goodService.deleteOrder(id);
    }

    // Check availability before ordering
    @GetMapping("/check-availability/{itemId}/{quantity}")
    public GoodResponse checkAvailability(@PathVariable Integer itemId,
                                          @PathVariable Integer quantity) {
        return goodService.checkItemAvailability(itemId, quantity);
    }
}