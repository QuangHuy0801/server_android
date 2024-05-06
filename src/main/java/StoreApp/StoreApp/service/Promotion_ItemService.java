package StoreApp.StoreApp.service;

import StoreApp.StoreApp.entity.Promotion_Item;

public interface Promotion_ItemService {
	Promotion_Item savePromotion_Item(Promotion_Item promotion_Item);
	
	void deleteById(int id);
	
	void deleteByPromotionId(int id);
	
	Promotion_Item getPromotionItemById(int id);
}
