package StoreApp.StoreApp.service;

import java.sql.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import StoreApp.StoreApp.entity.Order_Item;
import StoreApp.StoreApp.entity.Product;
import StoreApp.StoreApp.entity.ReportTotal;

public interface ProductService {
	List<Product> getAllProduct();
	
	Product saveProduct(Product product);

	Product getProductById(int id);

	Product updateProduct(Product product);

	void deleteProductById(int id);
	
	List<Product> findByProduct_NameContaining(String name);
	
	List<Product> findTop12ProductBestSellers();
	
	List<Product> findTop12ProductNewArrivals();

	Page<Product> findAll(Pageable pageable);

	Page<Product> findByProduct_NameContaining(String name, Pageable pageable);

	Page<Product> findByProduct_NameAndCategory_idContaining(String name, int category_id, Pageable pageable);

	List<Product> findTop4ProductByCategory_id(int name);
	List<Object[]> findRevenueStatisticByDate(Date datefrom,Date dateto);
	List<Object[]> findQuantityStatisticByDate(Date datefrom,Date dateto);
	List<Object[]> findProductStatistic();
	List<Object[]> findUnitOfProductStatistic();
	List<Object[]> findStatisticByMonth(int year);
	
	List<Product> getProductNotInPromotion();
}
