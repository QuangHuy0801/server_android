package StoreApp.StoreApp.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;

import StoreApp.StoreApp.entity.Category;
import StoreApp.StoreApp.entity.Order;
import StoreApp.StoreApp.entity.Order_Item;
import StoreApp.StoreApp.entity.Product;
import StoreApp.StoreApp.entity.ProductImage;
import StoreApp.StoreApp.entity.User;
import StoreApp.StoreApp.model.Mail;
import StoreApp.StoreApp.model.OrderDto;
import StoreApp.StoreApp.entity.ReportTotal;
import StoreApp.StoreApp.service.CategoryService;
import StoreApp.StoreApp.service.CloudinaryService;
import StoreApp.StoreApp.service.MailService;
import StoreApp.StoreApp.service.OrderService;
import StoreApp.StoreApp.service.Order_ItemService;
import StoreApp.StoreApp.service.ProductImageService;
import StoreApp.StoreApp.service.ProductService;
import StoreApp.StoreApp.service.UserService;

@Controller
public class AdminController {
	@Autowired
	OrderService orderService;

	@Autowired
	UserService userService;

	@Autowired
	ProductService productService;

	@Autowired
	CategoryService categoryService;

	@Autowired
	Order_ItemService order_ItemService;

	@Autowired
	CloudinaryService cloudinaryService;

	@Autowired
	MailService mailService;

	@Autowired
	HttpSession session;

	@Autowired
	ProductImageService productImageService;

	@GetMapping(path = "/loginAdmin")
	public ResponseEntity<User> Login(String id, String password) {
		System.out.println(id);
		User userFind = userService.findByIdAndRole(id, "admin");
		
		if (userFind != null && userFind.getPassword() != null) {
			String decodedValue = new String(Base64.getDecoder().decode(userFind.getPassword()));
			System.out.println(userFind);
			if (password.equals(decodedValue)) {
//			userFind.setPassword(decodedValue);
				userFind.setPassword(decodedValue);
				return new ResponseEntity<>(userFind, HttpStatus.OK);
			}
			else {
				return null;
			}
		}
		else
			return new ResponseEntity<>(userFind, HttpStatus.OK);
	}
	@PostMapping(path = "changepasswordadmin", consumes = "application/x-www-form-urlencoded")
	public ResponseEntity<String> ChangePassword(String id, String password) {
		User user = userService.findByIdAndRole(id, "admin");
		if (user != null) {
			String encodedValue = Base64.getEncoder().encodeToString(password.getBytes());
			user.setPassword(encodedValue);
			userService.saveUser(user);
			return new ResponseEntity<String>(password, HttpStatus.OK);
		} else
			return new ResponseEntity<String>(HttpStatus.NOT_ACCEPTABLE);
	}
	
	@PostMapping(path = "updateadmin", consumes = "multipart/form-data")
	public ResponseEntity<User> UpdateAvatar(String id, MultipartFile avatar, String fullname, String email,
			String phoneNumber, String address) {
		User user = userService.findByIdAndRole(id, "admin");
		if (user != null) {
			if (avatar !=null) {
				String url = cloudinaryService.uploadFile(avatar);
				user.setAvatar(url);
			}
			user.setUser_Name(fullname);
			user.setEmail(email);
			user.setPhone_Number(phoneNumber);
			user.setAddress(address);
			userService.saveUser(user);
			if(user.getPassword()!=null)
				user.setPassword(new String(Base64.getDecoder().decode(user.getPassword())));
			return new ResponseEntity<User>(user, HttpStatus.OK);
		} else {
			return new ResponseEntity<User>(HttpStatus.NOT_ACCEPTABLE);
		}
	}
	@PostMapping(path = "/forgotadmin", consumes = "application/x-www-form-urlencoded")
	public ResponseEntity<String> ForgotPassword(String id) {
		User user = userService.findByIdAndRole(id, "admin");
		if (user != null) {
			int code = (int) Math.floor(((Math.random() * 899999) + 100000));
			Mail mail = new Mail();
			mail.setMailFrom("n20dccn022@student.ptithcm.edu.vn");
			mail.setMailTo(user.getEmail());
			mail.setMailSubject("For got Password");
			mail.setMailContent("Your code is: " + code);
			mailService.sendEmail(mail);
			session.setAttribute("code", code);
			System.out.println(code);
			return new ResponseEntity<String>(new Gson().toJson(String.valueOf(code)), HttpStatus.OK);
		} else
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
	}
	@PostMapping(path = "/forgotnewpassadmin", consumes = "application/x-www-form-urlencoded")
	public ResponseEntity<String> ForgotNewPass(String id, String code, String password) {
//		String codeSession = (String) session.getAttribute("code");
//		System.out.println("session: "+ codeSession);
		User user = userService.findByIdAndRole(id, "admin");
		if (user != null) {
			String encodedValue = Base64.getEncoder().encodeToString(password.getBytes());
			user.setPassword(encodedValue);
			userService.saveUser(user);
			return new ResponseEntity<String>(password, HttpStatus.OK);
		} else
			return new ResponseEntity<String>(HttpStatus.NOT_ACCEPTABLE);
	}
	
//	@GetMapping(path = "/age-group")
//    public ResponseEntity<List<ReportTotal>> ageGroup() {
//  
//            // Gọi phương thức để thực hiện xử lý với các đối tượng Date này
//            List<Object[]> results = userService.findAgeGroup();
//            List<ReportTotal> reportTotals = new ArrayList<>();
//            System.out.println(results);
//            for (Object[] result : results) {
//                String name = (String) result[0];
//                BigDecimal value = (BigDecimal) result[1];
//                reportTotals.add(new ReportTotal(name, value.doubleValue()));
//            }
//
//            if (reportTotals.isEmpty()) {
//                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            } else {
//                return new ResponseEntity<>(reportTotals, HttpStatus.OK);
//        }
//    }
//	@GetMapping(path = "/age-group-revenue")
//    public ResponseEntity<List<Object[]>> ageGroupRevenue() {
//  
//            // Gọi phương thức để thực hiện xử lý với các đối tượng Date này
//            List<Object[]> results = userService.findAgeGroupRevenue();
//            if (results.isEmpty()) {
//                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            } else {
//                return new ResponseEntity<>(results, HttpStatus.OK);
//        }
//    }
	
	@GetMapping(path = "/revenue-statistic")
    public ResponseEntity<List<ReportTotal>> reportTotal(@RequestParam("dateFrom") String dateFromString,
                                                         @RequestParam("dateTo") String dateToString) {
       
            Date dateFrom = java.sql.Date.valueOf(dateFromString);
            Date dateTo = java.sql.Date.valueOf(dateToString);


            // Gọi phương thức để thực hiện xử lý với các đối tượng Date này
            List<Object[]> results = productService.findRevenueStatisticByDate(dateFrom, dateTo);
            List<ReportTotal> reportTotals = new ArrayList<>();
            System.out.println(results);
            for (Object[] result : results) {
                String name = (String) result[0];
                BigDecimal value = (BigDecimal) result[1];
                reportTotals.add(new ReportTotal(name, value.doubleValue()));
            }

            if (reportTotals.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(reportTotals, HttpStatus.OK);
        }
    }
	@GetMapping(path = "/month-statistic")
    public ResponseEntity<List<ReportTotal>> monthStatistic(@RequestParam("year") String year) {
            // Gọi phương thức để thực hiện xử lý với các đối tượng Date này
		int yearInt = Integer.parseInt(year); 
            List<Object[]> results = productService.findStatisticByMonth(yearInt);
            List<ReportTotal> reportTotals = new ArrayList<>();
            System.out.println(results);
            for (Object[] result : results) {
                String name = String.valueOf( result[0]);
                BigDecimal value = (BigDecimal) result[1];
                reportTotals.add(new ReportTotal(name, value.doubleValue()));
            }

            if (reportTotals.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(reportTotals, HttpStatus.OK);
        }
    }
	@GetMapping(path = "/quantity-statistic")
    public ResponseEntity<List<ReportTotal>> quantityStatistic(@RequestParam("dateFrom") String dateFromString,
                                                         @RequestParam("dateTo") String dateToString) {
       
            Date dateFrom = java.sql.Date.valueOf(dateFromString);
            Date dateTo = java.sql.Date.valueOf(dateToString);


            // Gọi phương thức để thực hiện xử lý với các đối tượng Date này
            List<Object[]> results = productService.findQuantityStatisticByDate(dateFrom, dateTo);
            List<ReportTotal> reportTotals = new ArrayList<>();
            System.out.println(results);
            for (Object[] result : results) {
                String name = (String) result[0];
                BigDecimal value = (BigDecimal) result[1];
                reportTotals.add(new ReportTotal(name, value.doubleValue()));
            }

            if (reportTotals.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(reportTotals, HttpStatus.OK);
        }
    }
	@GetMapping(path = "/product-statistic")
    public ResponseEntity<List<ReportTotal>> ProductStatistic() {
            // Gọi phương thức để thực hiện xử lý với các đối tượng Date này
            List<Object[]> results = productService.findProductStatistic();
            List<ReportTotal> reportTotals = new ArrayList<>();
            for (Object[] result : results) {
                String name = (String) result[0];
                BigDecimal value = (BigDecimal) result[1];
                reportTotals.add(new ReportTotal(name, value.doubleValue()));
            }
            if (reportTotals.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(reportTotals, HttpStatus.OK);
        }
    }
	@GetMapping(path = "/unit-of-product-statistic")
    public ResponseEntity<List<ReportTotal>> UnitOfProductStatistic() {
            // Gọi phương thức để thực hiện xử lý với các đối tượng Date này
            List<Object[]> results = productService.findUnitOfProductStatistic();
            List<ReportTotal> reportTotals = new ArrayList<>();
            for (Object[] result : results) {
                String name = (String) result[0];
                BigDecimal value = (BigDecimal) result[1];
                reportTotals.add(new ReportTotal(name, value.doubleValue()));
            }
            if (reportTotals.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(reportTotals, HttpStatus.OK);
        }
	}
	@GetMapping("/signin-admin")
	public String SignInAdminView(Model model) {
		String err_sign_admin = (String) session.getAttribute("err_sign_admin");
		model.addAttribute("err_sign_admin", err_sign_admin);
		session.setAttribute("err_sign_admin", null);
		return "signin-admin";
	}

	@PostMapping("/signin-admin")
	public String SignInAdminHandel(@ModelAttribute("login-name") String login_name,
			@ModelAttribute("pass") String pass, Model model) throws Exception {
		User admin = userService.findByIdAndRole(login_name, "admin");
		System.out.println(admin);
		if (admin == null) {
			session.setAttribute("err_sign_admin", "Username or Password is not correct!");
			return "redirect:/signin-admin";
		} else {
			String decodedValue = new String(Base64.getDecoder().decode(admin.getPassword()));
			if (!decodedValue.equals(pass)) {
				session.setAttribute("err_sign_admin", "Username or Password is not correct!");
				return "redirect:/signin-admin";
			} else {
				System.out.println(admin);
				session.setAttribute("admin", admin);
				return "redirect:/dashboard";
			}
		}
	}

	@GetMapping("/logout-admin")
	public String LogOutAdmin(Model model) {
		session.setAttribute("admin", null);
		return "redirect:/signin-admin";
	}

	@GetMapping("/dashboard")
	public String DashboardView(Model model) {
		User admin = (User) session.getAttribute("admin");
		System.out.println("======");
		if (admin == null) {
			return "redirect:/signin-admin";
		} else {
			List<Order> listOrder = orderService.findAll();
			List<Product> listProduct = productService.getAllProduct();
			List<User> listUser = userService.findAll();
			List<Category> listCategory = categoryService.findAll();

			model.addAttribute("Total_Order", listOrder.size());
			model.addAttribute("Total_Product", listProduct.size());
			model.addAttribute("Total_User", listUser.size());
			model.addAttribute("Total_Category", listCategory.size());
			return "dashboard";
		}
	}

	@GetMapping("/dashboard-invoice/{id}")
	public String InvoiceView(@PathVariable int id, Model model, HttpServletRequest request) {
		Order order = orderService.findById(id);
		List<Order_Item> listOrder_Item = order_ItemService.getAllByOrder_Id(order.getId());
		model.addAttribute("listOrder_Item", listOrder_Item);
		model.addAttribute("order", order);
		return "dashboard-invoice";
	}

	@GetMapping("/dashboard-orders")
	public String DashboardOrderView(Model model) {
		User admin = (User) session.getAttribute("admin");
		if (admin == null) {
			return "redirect:/signin-admin";
		} else {
			Pageable pageable = PageRequest.of(0, 3);
			Page<Order> pageOrder = orderService.findAll(pageable);
			model.addAttribute("pageOrder", pageOrder);
			return "dashboard-orders";
		}
	}

	@GetMapping("/dashboard-orders/{page}")
	public String DashboardOrderPageView(@PathVariable int page, Model model) {
		User admin = (User) session.getAttribute("admin");
		if (admin == null) {
			return "redirect:/signin-admin";
		} else {
			Pageable pageable = PageRequest.of(page, 3);
			Page<Order> pageOrder = orderService.findAll(pageable);
			model.addAttribute("pageOrder", pageOrder);
			return "dashboard-orders";
		}
	}

	@PostMapping("/send-message")
	public String SendMessage(Model model, @ModelAttribute("message") String message,
			@ModelAttribute("email") String email, HttpServletRequest request) throws Exception {
		String referer = request.getHeader("Referer");
		System.out.println(message);
		System.out.println(email);
		Mail mail = new Mail();
		mail.setMailFrom("n20dccn022@student.ptithcm.edu.vn");
		mail.setMailTo(email);
		mail.setMailSubject("This is message from Male fashion.");
		mail.setMailContent(message);
		mailService.sendEmail(mail);
		return "redirect:" + referer;
	}

	@GetMapping("/delete-order/{id}")
	public String DeleteOrder(@PathVariable int id, Model model, HttpServletRequest request) throws Exception {
		User admin = (User) session.getAttribute("admin");
		if (admin == null) {
			return "redirect:/signin-admin";
		} else {
			String referer = request.getHeader("Referer");
			Order order = orderService.findById(id);
			System.out.println(order);
			if (order != null) {
				for (Order_Item y : order.getOrder_Item()) {
					order_ItemService.deleteById(y.getId());
				}
				orderService.deleteById(id);
			}
			return "redirect:" + referer;
		}
	}

	@GetMapping("dashboard-wallet")
	public String DashboardWalletView(Model model) {
		User admin = (User) session.getAttribute("admin");
		if (admin == null) {
			return "redirect:/signin-admin";
		} else {
			List<Order> listOrder = orderService.findAll();
			List<Order> listPaymentWithMomo = orderService.findAllByPayment_Method("Pay with ZaloPay");
			List<Order> listPaymentOnDelivery = orderService.findAllByPayment_Method("Pay on Delivery");
			int TotalMomo = 0;
			int TotalDelivery = 0;
			for (Order y : listPaymentWithMomo) {
				TotalMomo = TotalMomo + y.getTotal();
			}
			for (Order y : listPaymentOnDelivery) {
				TotalDelivery = TotalDelivery + y.getTotal();
			}
			model.addAttribute("TotalMomo", TotalMomo);
			model.addAttribute("TotalDelivery", TotalDelivery);
			model.addAttribute("TotalOrder", listOrder.size());
			return "dashboard-wallet";
		}
	}
	
	@GetMapping("/redirect")
	public String Redirect(Model model, HttpServletRequest request) {
		String referer = request.getHeader("Referer");
		return "redirect:" + referer;
	}

}
