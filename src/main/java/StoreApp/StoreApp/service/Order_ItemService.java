package StoreApp.StoreApp.service;

import java.sql.Date;
import java.util.List;

import StoreApp.StoreApp.entity.Order_Item;
import StoreApp.StoreApp.entity.ReportTotal;

public interface Order_ItemService {

	List<Order_Item> getAllByOrder_Id(int id);
	public Order_Item saveOrder_Item(Order_Item order_Item);
	void deleteById(int id);
}
