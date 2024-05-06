package StoreApp.StoreApp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import StoreApp.StoreApp.entity.Cart;
import StoreApp.StoreApp.entity.Category;
import StoreApp.StoreApp.entity.Order;
import StoreApp.StoreApp.entity.Product;
import StoreApp.StoreApp.model.OrderDto;
import StoreApp.StoreApp.service.CategoryService;
import StoreApp.StoreApp.service.CloudinaryService;


@RestController
public class CategoryController {
	@Autowired
	CategoryService categoryService;
	@Autowired
	CloudinaryService cloudinaryService;

	@GetMapping(path = "/category")
	public ResponseEntity<List<Category>> GetCategory(){
		List<Category> listcaCategories = categoryService.findAll();
		return new ResponseEntity<>(listcaCategories, HttpStatus.OK);
	}
	@Autowired
    private ModelMapper modelMapper;
	

//     @PostMapping(path = "/newcategory")
//	public ResponseEntity<Category> newCategory(String category_name, MultipartFile category_image){
//		Category newCategory = new Category();
//	    newCategory.setCategory_Name(category_name);
//	    if (category_image != null) {
//			String url = cloudinaryService.uploadFile(category_image);
//			newCategory.setCategory_Image(url);
//		}
//	   
//	    Category savedCategory = categoryService.saveCategory(newCategory);
//	    return new ResponseEntity<Category>(savedCategory, HttpStatus.OK);
//	}
    @PostMapping(path = "/newcategory")
    public ResponseEntity<Category> newCategory(String category_name, MultipartFile category_image) {
        try {
            Category newCategory = new Category();
            newCategory.setCategory_Name(category_name);
            
            // Kiểm tra xem hình ảnh danh mục có tồn tại không
            if (category_image != null) {
                String url = cloudinaryService.uploadFile(category_image);
                newCategory.setCategory_Image(url);
               	System.out.println(2);
            } else {
                // Nếu không có hình ảnh được tải lên, trả về lỗi không hợp lệ
               	System.out.println(3);
                return ResponseEntity.badRequest().build();
            }
            System.out.println(newCategory);
            Category savedCategory = categoryService.saveCategory(newCategory);
            return new ResponseEntity<Category>(savedCategory, HttpStatus.OK);
        } catch (Exception e) {
            // Nếu có lỗi xảy ra, trả về một phản hồi lỗi
           	System.out.println(4);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
	
	@PutMapping(path = "/updatecategory", consumes = "multipart/form-data")
	public ResponseEntity<Category> updateCategory(int id, String category_name, MultipartFile category_image){
		Category category = categoryService.getCategoryById(id);
		if (category != null) {
			if (category_image != null) {
				String url = cloudinaryService.uploadFile(category_image);
				category.setCategory_Image(url);
			}
			if (category_name != null) {
				category.setCategory_Name(category_name);
			}						
			categoryService.saveCategory(category);
			return new ResponseEntity<Category>(category, HttpStatus.OK);
		}
		else {
			return new ResponseEntity<Category>(HttpStatus.NOT_ACCEPTABLE);
		}		
	}
	
	@DeleteMapping("/deletecategory/{id}")
	public ResponseEntity<Object> deleteCategory(@PathVariable Integer id) {
	    Category category = categoryService.getCategoryById(id);
	    if (category != null) {
	        categoryService.deleteCategoryId(id);
	        Map<String, String> response = new HashMap<>();
         response.put("message", "Category with ID " + id + " has been deleted");
         return new ResponseEntity<>(response, HttpStatus.OK);
	    } else {
	        Map<String, String> response = new HashMap<>();
	        response.put("error", "Category with ID " + id + " not found");
	        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	    }
	}
	
}
