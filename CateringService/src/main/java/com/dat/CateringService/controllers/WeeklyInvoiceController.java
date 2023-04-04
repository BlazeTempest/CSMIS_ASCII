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

	/* generate voucher_ID like CS001-currentdate */
	private static int counter = 1;

	private String generateSuffix() {

		System.out.println("generateSuffix() called, counter = " + counter);
		String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

		return String.format("%03d-%s", counter, dateStr);
	}

	@RequestMapping("/weekly-invoice")
	public String weeklyInvoice(Model model, PaymentVoucher paymentVoucher,
			@RequestParam(name = "invoiceStart", required = false) String startDate,
			@RequestParam(name = "invoiceEnd", required = false) String endDate,
			@RequestParam(name = "paymentDate", required = false) String paymentDate,
			@RequestParam(name = "voucherID", required = false) String voucherID, RedirectAttributes re) {

		List<Restaurant> restaurantNameList=restaurantService.getRestaurantName();
		model.addAttribute("restaurantNameList",restaurantNameList);
		
		List<Staff> adminTeam=staffService.getAdminTeam();
		model.addAttribute("adminTeam",adminTeam);
	
		String lastInsertedDate = weeklyInvoiceService.findLastInsertedToDate();
		System.out.println("This is last inserted date from  weekly invoice >>>>>>>>>>" + lastInsertedDate);

		int totalPrice = priceService.findActivePrice().getTotal_price();

		int totalAmount;

		int totalCost = 0;

		int totalRegisterCount = 0;
		int totalActualCount = 0;
		int totalDifferenceCount = 0;

		List<Headcount> dailyInvoiceList = headCountService.findAll();

		List<WeeklyInvoiceDTO> dailyInvoiceList1 = new ArrayList<>();
		for (Headcount headcount : dailyInvoiceList) {
			int dailyTotalAmount = 0;

			WeeklyInvoiceDTO temp = new WeeklyInvoiceDTO();

			if (headcount.getActualCount() >= headcount.getRegisteredCount()) {
				dailyTotalAmount = headcount.getActualCount() * totalPrice;

			} else {
				dailyTotalAmount = headcount.getRegisteredCount() * totalPrice;

			}

			temp.setInvoiceDate(headcount.getInvoiceDate());
			temp.setRegisteredCount(headcount.getRegisteredCount());
			temp.setActualCount(headcount.getActualCount());
			temp.setDifference(headcount.getDifference());
			temp.setTotalPrice(totalPrice);
			temp.setTotalAmount(dailyTotalAmount);

			dailyInvoiceList1.add(temp);
		}
		model.addAttribute("dailyInvoiceList", dailyInvoiceList1);
		model.addAttribute("voucherID", voucherID);
		model.addAttribute("invoiceStart", startDate);
		model.addAttribute("invoiceEnd", endDate);
		model.addAttribute("paymentDate", paymentDate);

		PaymentVoucher weeklyInvoice = new PaymentVoucher();

		// System.out.println(startDate + " : " + endDate);

		int numberOfPax = 0;

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
				temp.setTotalPrice(totalPrice);

				if (headcount.getActualCount() > headcount.getRegisteredCount()) {
					totalAmount = headcount.getActualCount() * totalPrice;
					numberOfPax += headcount.getActualCount();
				} else {
					totalAmount = headcount.getRegisteredCount() * totalPrice;
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
		model.addAttribute("numberOfPax", numberOfPax);
		model.addAttribute("totalCost", totalCost);
		model.addAttribute("totalRegisterCount", totalRegisterCount);
		model.addAttribute("totalActualCount", totalActualCount);
		model.addAttribute("totalDifferenceCount", totalDifferenceCount);
		model.addAttribute("restaurantName", restaurantService.findActiveRestaurantName());
		model.addAttribute("received", restaurantService.findRestaurantReceiverName());
		model.addAttribute("weeklyInvoice", weeklyInvoice);

		// weeklyInvoiceService.save(paymentVoucher);
		return "admin/weekly-invoice";

	}

	@PostMapping("/saveWeeklyInvoice")
	public String saveWeeklyInvoice(
			@ModelAttribute("weeklyInvoice") 
			PaymentVoucher paymentVoucher, Model model, RedirectAttributes re) {

		String lastInsertedDate = weeklyInvoiceService.findLastInsertedToDate();
		System.out.println("This is last inserted date from save weekly invoice >>>>>>>>>>" + lastInsertedDate);

		paymentVoucher.setVoucher_ID("CS" + generateSuffix());
		System.out.println("CS" + generateSuffix());

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String start_date_str = paymentVoucher.getStart_date().format(formatter);
		String end_date_str = paymentVoucher.getEnd_date().format(formatter);

		String payment_date = paymentVoucher.getPayment_date().format(formatter);
		
		LocalDate weekendDay = LocalDate.parse(start_date_str);
		DayOfWeek dayOfWeek = weekendDay.getDayOfWeek();

		if (lastInsertedDate == null) {

			
			if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
				System.out.println(dayOfWeek + " is a weekend day !");
				 re.addFlashAttribute("invoiceStartDateError", start_date_str +
						 " is weekend day!");
						
				return "redirect:/weekly-invoice";
			} else {

				re.addFlashAttribute("voucherID", paymentVoucher.getVoucher_ID());
				re.addFlashAttribute("restaurantName", paymentVoucher.getRestaurant_name());

				re.addFlashAttribute("paymentDate", paymentVoucher.getPayment_date());

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

			re.addFlashAttribute("paymentDate", paymentVoucher.getPayment_date());

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
			  System.out.println("This is last inserted date from error>>>>>>" +formattedDate1);
			re.addFlashAttribute("incvoiceError", " A voucher had already been created prior to " + formattedDate1 + " .");
			return "redirect:/weekly-invoice";
		}
	}

	@PostMapping("/confirm")
	public String saveInvoice(RedirectAttributes re, @RequestParam("voucherID") String voucherID,
			@RequestParam("paymentDate") String paymentDate,
			@RequestParam("invoiceStart") String invoiceStart, @RequestParam("invoiceEnd") String invoiceEnd,
			@RequestParam("totalCost") int totalCost, @RequestParam("received") String received,
			@RequestParam("selectedCashier") String selectedCashier, @RequestParam("selectedApprover")String selectedApprover,
			@RequestParam("selectedRestaurantName")String selectedRestaurantName,
			@RequestParam("paymentMethod") String paymentMethod, @RequestParam("numberOfPax") int numberOfPax,
			PaymentVoucher paymentVoucher, Authentication authentication) {
		
		counter++;
		
		int totalPrice = priceService.findActivePrice().getTotal_price();
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
		paymentVoucher.setStart_date(invoiceStart1);
		paymentVoucher.setEnd_date(invoiceEnd1);
		paymentVoucher.setRestaurant_name(selectedRestaurantName);
		paymentVoucher.setCreated_date(LocalDate.now());
		paymentVoucher.setReceived_by(received);
		paymentVoucher.setCashier(selectedCashier);
		paymentVoucher.setApproved_by(selectedApprover);
		paymentVoucher.setTotalPrice(totalPrice);
		paymentVoucher.setPayment_method(paymentMethod);
		paymentVoucher.setNo_of_pax(numberOfPax);
		paymentVoucher.setTotalCost(totalCost);
		paymentVoucher.setStatus("paid");
		paymentVoucher.setPayment_date(paymentDate1);

		/* paymentVoucher.setPayment_date(paymentDate); */
		weeklyInvoiceService.save(paymentVoucher);

		re.addFlashAttribute("invoiceSuccessMessage",
				voucherID + "From " + invoiceStart1 + " to " + invoiceEnd1 + " voucher created successfully!");

		return "redirect:/weekly-invoice";
	}
}