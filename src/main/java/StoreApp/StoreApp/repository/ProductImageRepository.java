package StoreApp.StoreApp.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import StoreApp.StoreApp.entity.ProductImage;

public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {

	void deleteById(int id);

	@Modifying
	@Transactional
	@Query("DELETE FROM ProductImage pi WHERE pi.product.id =?1")
	void deleteProductImagesByProductId(int productId);

}
