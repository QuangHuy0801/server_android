package StoreApp.StoreApp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import StoreApp.StoreApp.entity.Category;
import StoreApp.StoreApp.entity.Product;
import StoreApp.StoreApp.entity.Promotion;
import StoreApp.StoreApp.repository.PromotionRepository;
import StoreApp.StoreApp.service.PromotionService;

@Service
public class PromotionServiceImpl implements PromotionService{
	@Autowired
	PromotionRepository promotionRepository;
	
	@Override
	public Promotion savePromotion(Promotion promotion) {
		return promotionRepository.save(promotion);
	}

	@Override
	public Promotion getPromotionById(int id) {
		// TODO Auto-generated method stub
		return promotionRepository.getById(id);
	}
	
	@Override
	public void deletePromotionById(int id) {
		promotionRepository.deleteById(id);
	}

	@Override
	public List<Promotion> findAll() {
		return promotionRepository.findAll();
	}

	@Override
	public Promotion getPromotionByProductId(int productId) {
		return promotionRepository.getPromotionByProductId(productId);
	}
}
