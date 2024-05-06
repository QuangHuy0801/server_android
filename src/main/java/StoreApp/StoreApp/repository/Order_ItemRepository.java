package StoreApp.StoreApp.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import StoreApp.StoreApp.entity.Order;
import StoreApp.StoreApp.entity.Order_Item;
import StoreApp.StoreApp.entity.ReportTotal;

public interface Order_ItemRepository extends JpaRepository<Order_Item,Integer>{

	List<Order_Item> findAllByOrder_id(int id);

	void deleteById(int id);
	
}
