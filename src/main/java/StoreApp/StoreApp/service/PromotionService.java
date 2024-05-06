package StoreApp.StoreApp.service;

import java.util.List;

import StoreApp.StoreApp.entity.Promotion;

public interface PromotionService{
	Promotion savePromotion(Promotion promotion);

	Promotion getPromotionById(int id);
	
	List<Promotion> findAll();
	
	Promotion getPromotionByProductId(int productId);

	void deletePromotionById(int id);
	
}
