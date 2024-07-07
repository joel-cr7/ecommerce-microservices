package com.ecommerce.microservices.product;


import com.ecommerce.microservices.exception.ProductPurchaseException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public Integer createProduct(ProductRequest request) {
        Product product = productMapper.toProduct(request);
        return productRepository.save(product).getId();
    }

    public List<ProductPurchaseResponse> purchaseProducts(List<ProductPurchaseRequest> request) {
        var productIds = request.stream().map(ProductPurchaseRequest::productId).toList();

        // using custom repository method, to check if requested productIds are present in DB
        // this will return the list of product Ids in db which are matching the requested product Ids
        var availableProducts = productRepository.findAllByIdInOrderById(productIds);

        if(productIds.size() != availableProducts.size()){
            throw new ProductPurchaseException("One or more products does not exist");
        }

        // sorting based on id
        var requestedProducts = request.stream()
                .sorted(Comparator.comparing(ProductPurchaseRequest::productId))
                .toList();

        var purchasedProducts = new ArrayList<ProductPurchaseResponse>();

        for(int i=0; i<availableProducts.size(); i++){
            Product availableProduct = availableProducts.get(i);
            ProductPurchaseRequest requestedProduct = requestedProducts.get(i);

            // check if available quantity is less than requested quantity
            if(availableProduct.getAvailableQuantity() < requestedProduct.quantity()){
                throw new ProductPurchaseException("Insufficient stock for product with ID:: "+availableProduct.getId());
            }

            // update the new available quantity
            var newAvailableQuantity = availableProduct.getAvailableQuantity() - requestedProduct.quantity();
            availableProduct.setAvailableQuantity(newAvailableQuantity);
            productRepository.save(availableProduct);

            purchasedProducts.add(productMapper.toProductPurchaseResponse(availableProduct, requestedProduct.quantity()));
        }

        return purchasedProducts;
    }

    public ProductResponse findProductById(Integer productId) {
        return productRepository.findById(productId)
                .map(productMapper::toProductResponse)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ith ID:: " + productId));
    }

    public List<ProductResponse> findAllProducts() {
        return productRepository.findAll()
                .stream().map(productMapper::toProductResponse)
                .toList();
    }
}
