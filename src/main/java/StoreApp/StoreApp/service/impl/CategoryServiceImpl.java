package StoreApp.StoreApp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import StoreApp.StoreApp.entity.Category;
import StoreApp.StoreApp.repository.CategoryRepository;
import StoreApp.StoreApp.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {
	@Autowired
	 CategoryRepository categoryRepository;
	
	@Override
	public Category saveCategory(Category category) {
		return categoryRepository.save(category);
	}

	@Override
	public Category getCategoryById(int id) {
		// TODO Auto-generated method stub
		return categoryRepository.getById(id);
	}

	@Override
	public Category updateCategory(Category category) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void deleteCategoryId(int id) {
		categoryRepository.deleteById(id);
	}

	@Override
	public List<Category> findAll() {
		return categoryRepository.findAll();
	}

}
