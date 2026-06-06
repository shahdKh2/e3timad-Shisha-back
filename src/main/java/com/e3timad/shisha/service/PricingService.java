package com.e3timad.shisha.service;

import com.e3timad.shisha.model.Product;
import com.e3timad.shisha.model.ProductOffer;
import com.e3timad.shisha.model.OfferType;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PricingService {

    public double calculateItemPrice(Product product, int quantity, List<ProductOffer> offers) {

        double normalPrice = product.getSellingPrice();

        double[] dp = new double[quantity + 1];
        Arrays.fill(dp, Double.MAX_VALUE);

        dp[0] = 0;

        for (int i = 1; i <= quantity; i++) {

            dp[i] = dp[i - 1] + normalPrice;

            for (ProductOffer offer : offers) {
                int offerQty = offer.getQuantity();
                double offerPrice = offer.getPrice();

                if (i >= offerQty) {
                    dp[i] = Math.min(dp[i], dp[i - offerQty] + offerPrice);
                }
            }
        }

        return dp[quantity];
    }

}