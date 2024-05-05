package com.web.apteka.service;

import com.web.apteka.model.AccountDTO;
import com.web.apteka.model.AidDTO;
import com.web.apteka.model.CartItemDTO;
import com.web.apteka.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CartService {
    @Autowired
    CartItemRepository cartRepository;

    public Integer getCartSize(UUID id){return cartRepository.getCartItemsCount(id);}

    public CartItemDTO addToCart(CartItemDTO item) {
        if(cartRepository.getCartItemExisted(item.getUser_id(),item.getAid_id())!=0) {
            cartRepository.addToCartExisted(item.getUser_id(), item.getAid_id(), item.getQuantity());
            return item;
        }
        else return cartRepository.save(item);
    }

    public List<AidDTO> getCartAids(UUID id){
        return cartRepository.getAidsFromCart(id);
    }
    public List<CartItemDTO> getCartItems(UUID id){
        return cartRepository.getCartItems(id);
    }
}
