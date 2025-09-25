package com.example.product.service;


import com.example.product.dto.ProductDTO;
import com.example.product.model.Product;
import com.example.product.repo.ProductRepo;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductService {
    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ModelMapper modelMapper;

    //Business logic for the getAll Products
    public List<ProductDTO> getAllProducts(){
        List<Product>productList = productRepo.findAll();
        return modelMapper.map(productList, new TypeToken<List<ProductDTO>>(){}.getType());
    }

    //Business logic for getProductById
    public ProductDTO getProductById(Integer productId) {
        Optional<Product> product = productRepo.findById(productId);
        if (product.isPresent()) {
            return modelMapper.map(product.get(), ProductDTO.class);
        } else {
            return null; // or throw a custom exception (recommended)
        }
    }

    //Business logic for the Save product
    public ProductDTO saveProduct(ProductDTO productDTO){
        productRepo.save(modelMapper.map(productDTO, Product.class));
        return productDTO;
    }

    //Business logic for the update product
    public ProductDTO updateProduct(ProductDTO productDTO){
        productRepo.save(modelMapper.map(productDTO, Product.class));
        return productDTO;
    }

    //Business Logic for the deleteProject
    public String deleteProduct(int id){
        if(productRepo.existsById(id)){
            productRepo.deleteById(id);
            return "Order Deleted Successfully !!!";
        } else {
            return "Order Not Found !!!";
        }
    }


}
