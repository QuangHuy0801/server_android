package StoreApp.StoreApp.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import StoreApp.StoreApp.entity.Cart;
import StoreApp.StoreApp.entity.Category;
import StoreApp.StoreApp.entity.Product;
import StoreApp.StoreApp.entity.ProductImage;
import StoreApp.StoreApp.service.CartService;
import StoreApp.StoreApp.service.CategoryService;
import StoreApp.StoreApp.service.CloudinaryService;
import StoreApp.StoreApp.service.ProductImageService;
import StoreApp.StoreApp.service.ProductService;
import lombok.val;

@RestController
public class ProductController {
	@Autowired
	ProductService productService;
	@Autowired
	ProductImageService productImageService;
	@Autowired
	CartService cartService;
	@Autowired
	CategoryService categoryService;
	@Autowired
	CloudinaryService cloudinaryService;
	
	@GetMapping(path = "/newproduct")
	public ResponseEntity<List<Product>> newProduct(){
		List<Product> newProducts = productService.findTop12ProductNewArrivals();
		return new ResponseEntity<>(newProducts, HttpStatus.OK);
	}
	
	@GetMapping(path = "/bestsellers")
	public ResponseEntity<List<Product>> bestSellers(){
		List<Product> bestSellers = productService.findTop12ProductBestSellers();
		return new ResponseEntity<>(bestSellers, HttpStatus.OK);
	}
	
	@GetMapping(path = "/search")
	public ResponseEntity<List<Product>> Search(String searchContent){
		List<Product> products = productService.findByProduct_NameContaining(searchContent);
		return new ResponseEntity<>(products, HttpStatus.OK);
	}
	
	@GetMapping(path = "/product")
	public ResponseEntity<List<Product>> GetProduct(){
		List<Product> listProduct = productService.getAllProduct();
		return new ResponseEntity<>(listProduct, HttpStatus.OK);
	}
    @PostMapping(path = "/addproduct",consumes = "multipart/form-data")
    public ResponseEntity<Product> newProduct(String product_name,
    		String product_price,
    		String product_quantity,
    		String product_decription,
    		String  product_category,
    		@RequestParam("product_images") List<MultipartFile> product_images,
    		String product_sold,
    		String product_is_selling,
    		String  product_is_active) {
    	System.out.println(product_images);
        try {
            Product newProduct = new Product();
            List<ProductImage> productImages = new ArrayList<ProductImage>();
//            List<String> imageUrls = new ArrayList<>();
            if (!product_images.isEmpty()) {
            	for(MultipartFile image :product_images) {
            	ProductImage productImage = new ProductImage();
                String url = cloudinaryService.uploadFile(image);
                productImage.setUrl_Image(url);
                productImages.add(productImage);
                }
            } else {
                return ResponseEntity.badRequest().build();
            }
        	java.sql.Date createdAt = new java.sql.Date(System.currentTimeMillis());
            newProduct.setProduct_Name(product_name);
            newProduct.setPrice(Integer.parseInt(product_price));
            newProduct.setQuantity(Integer.parseInt(product_quantity));
            newProduct.setDescription(product_decription);
            newProduct.setIs_Active(Integer.parseInt(product_is_active));
            newProduct.setIs_Selling(Integer.parseInt(product_is_selling));
            newProduct.setSold(Integer.parseInt(product_sold));
            newProduct.setCreated_At(createdAt);
            newProduct.setCategory(categoryService.getCategoryById(Integer.parseInt(product_category)));
            System.out.println(newProduct);
            Product savedProduct = productService.saveProduct(newProduct);
            for (ProductImage productImage : productImages) {
            	ProductImage productImage1=new ProductImage();
            	productImage1 =productImage;
            	productImage1.setProduct(savedProduct);
                productImageService.save(productImage1);
            }
            return new ResponseEntity<Product>(savedProduct, HttpStatus.OK);
        } catch (Exception e) {
           	System.out.println(4);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
	
    @PutMapping(path = "/updateproduct", consumes = "multipart/form-data")
    public ResponseEntity<Product> updateProduct(String id,
                                                String product_name,
                                         		String product_price,
                                         		String product_quantity,
                                         		String product_decription,
                                         		String  product_category,
                                         		@RequestParam("product_images") List<MultipartFile> product_images,
                                         		String product_sold,
                                         		String product_is_selling,
                                         		String  product_is_active) {
    	System.out.println(id);
    	System.out.println(product_name);
    	System.out.println(product_price);
    	System.out.println(product_quantity);
    	System.out.println(product_decription);
    	System.out.println(product_category);
    	System.out.println(product_images);
    	System.out.println(product_sold);
    	System.out.println(product_is_selling);
    	System.out.println(product_is_active);
        try {
            Product product = productService.getProductById(Integer.parseInt(id));
            List<ProductImage> productImages = new ArrayList<ProductImage>();
            if (product != null) {
            	 productImageService.deleteProductImagesByProductId(Integer.parseInt(id)); 
            	 if (!product_images.isEmpty()) {
                 	for(MultipartFile image :product_images) {
                 	ProductImage productImage = new ProductImage();
                     String url = cloudinaryService.uploadFile(image);
                     productImage.setUrl_Image(url);
                     productImages.add(productImage);
                     }
                 } else {
                     return ResponseEntity.badRequest().build();
                 }
                // Cập nhật thông tin sản phẩm
                product.setProduct_Name(product_name);
                product.setPrice(Integer.parseInt(product_price));
                product.setQuantity(Integer.parseInt(product_quantity));
                product.setDescription(product_decription);
                product.setIs_Active(Integer.parseInt(product_is_active));
                product.setIs_Selling(Integer.parseInt(product_is_selling));
                product.setSold(Integer.parseInt(product_sold));
                product.setCategory(categoryService.getCategoryById(Integer.parseInt(product_category)));
           
                for (ProductImage productImage : productImages) {
                	ProductImage productImage1=new ProductImage();
                	productImage1 =productImage;
                	productImage1.setProduct(product);
                    productImageService.save(productImage1);
                }

                // Lưu sản phẩm và hình ảnh sản phẩm
               
                for (ProductImage productImage : productImages) {
                    productImageService.save(productImage);
                }
                Product savedProduct = productService.saveProduct(product);

                return new ResponseEntity<Product>(savedProduct, HttpStatus.OK);
            } else {
            	return new ResponseEntity<Product>(HttpStatus.NOT_ACCEPTABLE);
            }
        } catch (Exception e) {
        	return new ResponseEntity<Product>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

	
	@DeleteMapping("/deleteproduct/{id}")
	public ResponseEntity<Object> deleteProduct(@PathVariable Integer id) {
		Product product = productService.getProductById(id);
	    if (product != null) {
	    	productImageService.deleteProductImagesByProductId(id);
	    	productService.deleteProductById(id);
	        Map<String, String> response = new HashMap<>();
	        response.put("message", "Product with ID " + id + " has been deleted");
	        return new ResponseEntity<>(response, HttpStatus.OK);
	    } else {
	        Map<String, String> response = new HashMap<>();
	        response.put("error", "Product with ID " + id + " not found");
	        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	    }
	}
	
	@GetMapping(path = "/product/notInPromotion")
	public ResponseEntity<List<Product>> getProductNotInPromotion(){
		List<Product> list = productService.getProductNotInPromotion();
		if (list == null || list.isEmpty()) {
			return new ResponseEntity<>(list,HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
	
}
