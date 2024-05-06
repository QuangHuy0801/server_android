package StoreApp.StoreApp.service.impl;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import StoreApp.StoreApp.entity.Product;
import StoreApp.StoreApp.entity.ReportTotal;
import StoreApp.StoreApp.repository.ProductRepository;
import StoreApp.StoreApp.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	ProductRepository productRepository;

	@Override
	public List<Product> getAllProduct() {
		// TODO Auto-generated method stub
		return productRepository.findAll();
	}

	@Override
	public Product saveProduct(Product product) {
		// TODO Auto-generated method stub
		return productRepository.save(product);
	}

	@Override
	public Product getProductById(int id) {
		// TODO Auto-generated method stub
		return productRepository.findById(id);
	}

	@Override
	public Product updateProduct(Product product) {
		// TODO Auto-generated method stub
		return productRepository.save(product);
	}

	@Override
	public void deleteProductById(int id) {
		// TODO Auto-generated method stub
		productRepository.deleteById( id);
	}

	@Override
	public List<Product> findByProduct_NameContaining(String name) {
		// TODO Auto-generated method stub
		return productRepository.findByProduct_NameContaining(name);
	}

	@Override
	public List<Product> findTop12ProductBestSellers() {
		// TODO Auto-generated method stub
		return productRepository.findTop12ProductBestSellers();
	}

	@Override
	public List<Product> findTop12ProductNewArrivals() {
		// TODO Auto-generated method stub
		return productRepository.findTop12ProductNewArrivals();
	}

	@Override
	public Page<Product> findAll(Pageable pageable) {
		return productRepository.findAll(pageable);
	}

	@Override
	public Page<Product> findByProduct_NameAndCategory_idContaining(String name, int category_id, Pageable pageable) {
		return productRepository.findByProduct_NameAndCategory_idContaining(name, category_id, pageable);
	}

	@Override
	public Page<Product> findByProduct_NameContaining(String name, Pageable pageable) {
		return productRepository.findByProduct_NameContaining(name, pageable);
	}

	@Override
	public List<Product> findTop4ProductByCategory_id(int id) {
		return productRepository.findTop4ProductByCategory_id(id);
	}
	@Override
	public 	List<Object[]> findRevenueStatisticByDate(Date datefrom,Date dateto){
		return productRepository.findRevenueStatisticByDate(datefrom, dateto);
	}
	@Override
	public 	List<Object[]> findQuantityStatisticByDate(Date datefrom,Date dateto){
		return productRepository.findQuantityStatisticByDate(datefrom, dateto);
	}
	@Override
	public 	List<Object[]> findProductStatistic(){
		return productRepository.findProductStatistic();
	}
	@Override
	public 	List<Object[]> findUnitOfProductStatistic(){
		return productRepository.findUnitOfProductStatistic();
	}
	@Override
	public 	List<Object[]> findStatisticByMonth(int year){
		return productRepository.findStatisticByMonth(year);
	}

	@Override
	public List<Product> getProductNotInPromotion() {
		return productRepository.getProductNotInPromotion();
	}
	
}
