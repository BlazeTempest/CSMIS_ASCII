package com.dat.CateringService.controllers;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dat.CateringService.DTO.WeeklyInvoiceDTO;
import com.dat.CateringService.entity.Headcount;
import com.dat.CateringService.entity.PaymentVoucher;
import com.dat.CateringService.entity.Restaurant;
import com.dat.CateringService.entity.Staff;
import com.dat.CateringService.service.HeadcountService;
import com.dat.CateringService.service.PriceService;
import com.dat.CateringService.service.RestaurantService;
import com.dat.CateringService.service.StaffService;
import com.dat.CateringService.service.WeeklyInvoiceService;

@Controller
public class WeeklyInvoiceController {

	private RestaurantService restaurantService;
	
	private StaffService staffService;

	private PriceService priceService;

	private WeeklyInvoiceService weeklyInvoiceService;

	private HeadcountService headCountService;

	
	@Autowired
	public WeeklyInvoiceController(WeeklyInvoiceService weeklyInvoiceService, HeadcountService headCountService,
			RestaurantService restaurantService, PriceService priceService, StaffService staffService) {
		super();
		this.weeklyInvoiceService = weeklyInvoiceService;
		this.restaurantService = restaurantService;
		this.headCountService = headCountService;
		this.priceService = priceService;
		this.staffService=staffService;
	}

	/* generate voucher_ID like CS001-paymentdate */
	
	private static int id = 1;
	
	@RequestMapping("/weekly-invoice")
	public String weeklyInvoice(Authentication authentication,Model model, PaymentVoucher paymentVoucher,
			@RequestParam(name = "invoiceStart", required = false) String startDate,
			@RequestParam(name = "invoiceEnd", required = false) String endDate,
			@RequestParam(name = "paymentDate", required = false) String paymentDate,
			@RequestParam(name = "voucherID", required = false) String voucherID, RedirectAttributes re) {
		try {
			String role = authentication.getAuthorities().toArray()[0].toString();
			if (role.equals("admin")) {
				
				String generatedVoucherID;
				
				String lastInsertedVoucherID=weeklyInvoiceService.findLastInsertedVoucherID();
				
				List<Restaurant> restaurantNameList=restaurantService.getRestaurantName();
				model.addAttribute("restaurantNameList",restaurantNameList);
				
				List<Staff> adminTeam=staffService.getAdminTeam();
				model.addAttribute("adminTeam",adminTeam);
			
				String lastInsertedDate = weeklyInvoiceService.findLastInsertedToDate();
				System.out.println("This is last inserted date from  weekly invoice >>>>>>>>>>" + lastInsertedDate);

				int totalAmount;
				int totalCost = 0;

				int dailyTotalAmount = 0;
				int allDailyTotalAmount=0;
				int totalDailyActualCount=0;
				int totalDailyRegisteredCount=0;
				int totalDailyDiffCount=0;

				List<Headcount> dailyInvoiceList = headCountService.findAll();

				List<WeeklyInvoiceDTO> dailyInvoiceList1 = new ArrayList<>();
				for (Headcount headcount : dailyInvoiceList) {
					
					WeeklyInvoiceDTO temp = new WeeklyInvoiceDTO();
					
					totalDailyRegisteredCount+=headcount.getRegisteredCount();
					totalDailyDiffCount+=headcount.getDifference();
					totalDailyActualCount+=headcount.getActualCount();
					

					if (headcount.getActualCount() >= headcount.getRegisteredCount()) {
						dailyTotalAmount = headcount.getActualCount() * priceService.findById(headcount.getPrice()).getTotal_price();
						allDailyTotalAmount+=dailyTotalAmount;
						

					} else {
						dailyTotalAmount = headcount.getRegisteredCount() * priceService.findById(headcount.getPrice()).getTotal_price();
						allDailyTotalAmount+=dailyTotalAmount;
					}

					temp.setInvoiceDate(headcount.getInvoiceDate());
					temp.setRegisteredCount(headcount.getRegisteredCount());
					temp.setActualCount(headcount.getActualCount());
					temp.setDifference(headcount.getDifference());
					temp.setTotalPrice(priceService.findById(headcount.getPrice()).getTotal_price());
					temp.setTotalAmount(dailyTotalAmount);

					dailyInvoiceList1.add(temp);
				}
				
				if(lastInsertedVoucherID == null)
				{
				
					generatedVoucherID="CS"+ String.format("%03d", id) + '-'+ paymentDate;
					
					model.addAttribute("dailyInvoiceList", dailyInvoiceList1);
					model.addAttribute("noti", staffService.getStaffById(authentication.getName()).getEmail_noti());
					model.addAttribute("totalDailyActualCount",totalDailyActualCount);
					model.addAttribute("totalDailyDiffCount",totalDailyDiffCount);
					model.addAttribute("totalDailyRegisteredCount",totalDailyRegisteredCount);
					model.addAttribute("allDailyTotalAmount",allDailyTotalAmount);
					model.addAttribute("name", staffService.getStaffById(authentication.getName()).getName());
					model.addAttribute("voucherID", generatedVoucherID);
					model.addAttribute("invoiceStart", startDate);
					model.addAttribute("invoiceEnd", endDate);
					model.addAttribute("paymentDate", paymentDate);
				
			
				PaymentVoucher weeklyInvoice = new PaymentVoucher();

				int numberOfPax = 0;
				int totalRegisterCount = 0;
				int totalActualCount = 0;
				int totalDifferenceCount = 0;

				if (startDate != null) {
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
					List<Headcount> headcounts = headCountService.findByInvoiceDateBetween(
							LocalDate.parse(startDate, formatter), LocalDate.parse(endDate, formatter));

					List<WeeklyInvoiceDTO> dto = new ArrayList<>();

					for (Headcount headcount : headcounts) {
						WeeklyInvoiceDTO temp = new WeeklyInvoiceDTO();

						int registeredCount = headcount.getRegisteredCount();
						int actualCount = headcount.getActualCount();
						int differenceCount = headcount.getDifference();

						temp.setInvoiceDate(headcount.getInvoiceDate());
						temp.setRegisteredCount(registeredCount);
						temp.setActualCount(actualCount);
						temp.setDifference(differenceCount);
						temp.setTotalPrice(priceService.findById(headcount.getPrice()).getTotal_price());

						if (headcount.getActualCount() > headcount.getRegisteredCount()) {
							totalAmount = headcount.getActualCount() * priceService.findById(headcount.getPrice()).getTotal_price();
							numberOfPax += headcount.getActualCount();
						} else {
							totalAmount = headcount.getRegisteredCount() * priceService.findById(headcount.getPrice()).getTotal_price();
							numberOfPax += headcount.getRegisteredCount();
						}

						temp.setNumberOfPax(numberOfPax);
						temp.setTotalAmount(totalAmount);
						totalCost += totalAmount;

						totalRegisterCount += registeredCount;

						totalActualCount += actualCount;

						totalDifferenceCount += differenceCount;

						dto.add(temp);
					}
					model.addAttribute("dto", dto);
				}
				model.addAttribute("name", staffService.getStaffById(authentication.getName()).getName());
				model.addAttribute("noti", staffService.getStaffById(authentication.getName()).getEmail_noti());
				model.addAttribute("numberOfPax", numberOfPax);
				model.addAttribute("totalCost", totalCost);
				model.addAttribute("totalRegisterCount", totalRegisterCount);
				model.addAttribute("totalActualCount", totalActualCount);
				model.addAttribute("totalDifferenceCount", totalDifferenceCount);
				model.addAttribute("restaurantName", restaurantService.findActiveRestaurantName());
				model.addAttribute("received", restaurantService.findRestaurantReceiverName());
				model.addAttribute("weeklyInvoice", weeklyInvoice);

			
				 
				
				return "admin/weekly-invoice";
				}
				else {
					
					System.out.println("This is last inserted voucher ID >>>> "+lastInsertedVoucherID);
					
					String courseCode = lastInsertedVoucherID.substring(0, 2); // Extract "CS" from the start of the string
					String courseNumber = lastInsertedVoucherID.substring(2, 5); // Extract "001" from the middle of the string
					String date = lastInsertedVoucherID.substring(5); // Extract "2023-04-06" from the end of the string
					date = date.replace("-", "-"); // Replace "-" with "/" to match the desired format
					String result = courseCode + "   " + courseNumber + "     " + date;
					System.out.println(courseNumber);
					int courseNumber1=Integer.parseInt(courseCode);
					courseNumber1++;
					
					
					generatedVoucherID=courseCode + String.format("%03d", courseNumber1) +'-'+ paymentDate;
					
					System.out.println(generatedVoucherID);
					
					model.addAttribute("dailyInvoiceList", dailyInvoiceList1);
					model.addAttribute("noti", staffService.getStaffById(authentication.getName()).getEmail_noti());
					model.addAttribute("totalDailyActualCount", totalDailyActualCount);
					model.addAttribute("totalDailyDiffCount", totalDailyDiffCount);
					model.addAttribute("totalDailyRegisteredCount", totalDailyRegisteredCount);
					model.addAttribute("allDailyTotalAmount", allDailyTotalAmount);
					model.addAttribute("name", staffService.getStaffById(authentication.getName()).getName());
					 model.addAttribute("voucherID", generatedVoucherID); 
					model.addAttribute("invoiceStart", startDate);
					model.addAttribute("invoiceEnd", endDate);
					model.addAttribute("paymentDate", paymentDate);

					PaymentVoucher weeklyInvoice = new PaymentVoucher();

					int numberOfPax = 0;
					int totalRegisterCount = 0;
					int totalActualCount = 0;
					int totalDifferenceCount = 0;

					if (startDate != null) {
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
						List<Headcount> headcounts = headCountService.findByInvoiceDateBetween(
								LocalDate.parse(startDate, formatter), LocalDate.parse(endDate, formatter));

						List<WeeklyInvoiceDTO> dto = new ArrayList<>();

						for (Headcount headcount : headcounts) {
							WeeklyInvoiceDTO temp = new WeeklyInvoiceDTO();

							int registeredCount = headcount.getRegisteredCount();
							int actualCount = headcount.getActualCount();
							int differenceCount = headcount.getDifference();

							temp.setInvoiceDate(headcount.getInvoiceDate());
							temp.setRegisteredCount(registeredCount);
							temp.setActualCount(actualCount);
							temp.setDifference(differenceCount);
							temp.setTotalPrice(priceService.findById(headcount.getPrice()).getTotal_price());

							if (headcount.getActualCount() > headcount.getRegisteredCount()) {
								totalAmount = headcount.getActualCount()
										* priceService.findById(headcount.getPrice()).getTotal_price();
								numberOfPax += headcount.getActualCount();
							} else {
								totalAmount = headcount.getRegisteredCount()
										* priceService.findById(headcount.getPrice()).getTotal_price();
								numberOfPax += headcount.getRegisteredCount();
							}

							temp.setNumberOfPax(numberOfPax);
							temp.setTotalAmount(totalAmount);
							totalCost += totalAmount;

							totalRegisterCount += registeredCount;

							totalActualCount += actualCount;

							totalDifferenceCount += differenceCount;

							dto.add(temp);
						}
						model.addAttribute("dto", dto);
					}
					model.addAttribute("name", staffService.getStaffById(authentication.getName()).getName());
					model.addAttribute("noti", staffService.getStaffById(authentication.getName()).getEmail_noti());
					model.addAttribute("numberOfPax", numberOfPax);
					model.addAttribute("totalCost", totalCost);
					model.addAttribute("totalRegisterCount", totalRegisterCount);
					model.addAttribute("totalActualCount", totalActualCount);
					model.addAttribute("totalDifferenceCount", totalDifferenceCount);
					model.addAttribute("restaurantName", restaurantService.findActiveRestaurantName());
					model.addAttribute("received", restaurantService.findRestaurantReceiverName());
					model.addAttribute("weeklyInvoice", weeklyInvoice);

				;
					
					return "admin/weekly-invoice";
				}
			
			}return "404";
		}catch(NullPointerException e) {
			
			return "redirect:/showMyLoginPage";
		}
		
	}

	@PostMapping("/saveWeeklyInvoice")
	public String saveWeeklyInvoice(
			@ModelAttribute("weeklyInvoice") 
			PaymentVoucher paymentVoucher, Model model, RedirectAttributes re) {

		String lastInsertedDate = weeklyInvoiceService.findLastInsertedToDate();
		System.out.println("This is last inserted date from save weekly invoice >>>>>>>>>>" + lastInsertedDate);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String start_date_str = paymentVoucher.getStartDate().format(formatter);
		String end_date_str = paymentVoucher.getEndDate().format(formatter);

		String payment_date = paymentVoucher.getPaymentDate().format(formatter);
		
		LocalDate weekendDay = LocalDate.parse(start_date_str);
		DayOfWeek dayOfWeek = weekendDay.getDayOfWeek();

		if (lastInsertedDate == null) {

			if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
				System.out.println(dayOfWeek + " is a weekend day !");
				 re.addFlashAttribute("invoiceStartDateError", " Start Date (" + start_date_str +
						 ") is weekend day!");
						
				return "redirect:/weekly-invoice";
			} else {

				re.addFlashAttribute("voucherID", paymentVoucher.getVoucher_ID());
				re.addFlashAttribute("restaurantName", paymentVoucher.getRestaurant_name());

				re.addFlashAttribute("paymentDate", paymentVoucher.getPaymentDate());

				re.addAttribute("voucherID", paymentVoucher.getVoucher_ID());
				re.addAttribute("invoiceStart", start_date_str);
				re.addAttribute("invoiceEnd", end_date_str);
				re.addAttribute("paymentDate", payment_date);

				re.addFlashAttribute("invoiceStart", start_date_str);
				re.addFlashAttribute("invoiceEnd", end_date_str);
				
	
				
				return "redirect:/weekly-invoice";
			}
			
		} else if (LocalDate.parse(start_date_str).isAfter(LocalDate.parse(lastInsertedDate))
				&& dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY) {
			re.addFlashAttribute("voucherID", paymentVoucher.getVoucher_ID());
			re.addFlashAttribute("restaurantName", paymentVoucher.getRestaurant_name());

			re.addFlashAttribute("paymentDate", paymentVoucher.getPaymentDate());

			re.addAttribute("voucherID", paymentVoucher.getVoucher_ID());
			re.addAttribute("invoiceStart", start_date_str);
			re.addAttribute("invoiceEnd", end_date_str);
			re.addAttribute("paymentDate", payment_date);

			re.addFlashAttribute("invoiceStart", start_date_str);
			re.addFlashAttribute("invoiceEnd", end_date_str);

			re.addFlashAttribute("received", paymentVoucher.getReceived_by());


			return "redirect:/weekly-invoice";
		} else {
			 String dateStr = lastInsertedDate;
		        LocalDate date = LocalDate.parse(dateStr);
			  DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("MMMM d',' yyyy");
			  String formattedDate1 = date.format(formatter1);
			  
			re.addFlashAttribute("incvoiceError", " Voucher had already been created prior to " + formattedDate1 + " .");
			return "redirect:/weekly-invoice";
		}
	}

	@PostMapping("/confirm")
	public String saveInvoice(RedirectAttributes re, @RequestParam("voucherID") String voucherID,
			@RequestParam("paymentDate") String paymentDate,
			@RequestParam("invoiceStart") String invoiceStart, @RequestParam("invoiceEnd") String invoiceEnd,
			@RequestParam("totalCost") int totalCost, @RequestParam("selectedReceiverName") String selectedReceiverName,
			@RequestParam("selectedCashier") String selectedCashier, @RequestParam("selectedApprover")String selectedApprover,
			@RequestParam("selectedRestaurantName")String selectedRestaurantName,
			@RequestParam("paymentMethod") String paymentMethod, @RequestParam("numberOfPax") int numberOfPax,
			PaymentVoucher paymentVoucher, Authentication authentication) {
		
		List<Headcount> dailyInvoiceList = headCountService.findAll();
		
		int totalPrice=0;
	
		for (Headcount headcount : dailyInvoiceList) {
			totalPrice=priceService.findById(headcount.getPrice()).getTotal_price();
		}
		
		LocalDate paymentDate1 = LocalDate.parse(paymentDate);
		LocalDate invoiceStart1 = LocalDate.parse(invoiceStart);
		LocalDate invoiceEnd1 = LocalDate.parse(invoiceEnd);

		// System.out.println(invoiceStart1+ " "+ invoiceEnd1);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String start = invoiceStart1.format(formatter);
		String end = invoiceEnd1.format(formatter);

		String payDate = paymentDate1.format(formatter);

		re.addAttribute("invoiceStart", start);
		re.addAttribute("invoiceEnd", end);
		re.addAttribute("voucherID", voucherID);
		re.addAttribute("paymentDate", payDate);
		
		paymentVoucher.setCreated_by(staffService.getStaffById(authentication.getName()).getName());
		paymentVoucher.setVoucher_ID(voucherID);
		paymentVoucher.setStartDate(invoiceStart1);
		paymentVoucher.setEndDate(invoiceEnd1);
		paymentVoucher.setRestaurant_name(selectedRestaurantName);
		paymentVoucher.setCreated_date(LocalDate.now());
		paymentVoucher.setReceived_by(selectedReceiverName);
		paymentVoucher.setCashier(selectedCashier);
		paymentVoucher.setApproved_by(selectedApprover);
		paymentVoucher.setTotalPrice(totalPrice);
		paymentVoucher.setPayment_method(paymentMethod);
		paymentVoucher.setNo_of_pax(numberOfPax);
		paymentVoucher.setTotalCost(totalCost);
		paymentVoucher.setStatus("paid");
		paymentVoucher.setPaymentDate(paymentDate1);

		/* paymentVoucher.setPayment_date(paymentDate); */
		weeklyInvoiceService.save(paymentVoucher);
		
		id++;

		 
		re.addFlashAttribute("invoiceSuccessMessage",
				voucherID + " From " + invoiceStart1 + " to " + invoiceEnd1 + " voucher created successfully!");

		return "redirect:/weekly-invoice";
	}
}