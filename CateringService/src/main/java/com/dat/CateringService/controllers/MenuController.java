package com.dat.CateringService.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dat.CateringService.daos.AvoidMeatRepository;
import com.dat.CateringService.daos.PriceRepository;
import com.dat.CateringService.entity.AvoidMeat;
import com.dat.CateringService.entity.Price;
import com.dat.CateringService.entity.Staff;
import com.dat.CateringService.service.AvoidMeatService;
import com.dat.CateringService.service.MenuPdfService;
import com.dat.CateringService.service.PriceService;
import com.dat.CateringService.service.StaffService;

@Controller
public class MenuController {
	@Autowired
	private StaffService staffService;

	@Autowired
	private MenuPdfService menuPdfService;
	
	@Autowired
	private PriceRepository priecPriceRepository;

	@Autowired
	private AvoidMeatRepository avoidMeatRepository;

	private AvoidMeatService avoidMeatService;
	private PriceService priceService;

	public MenuController(AvoidMeatService theAvoidMeatService, PriceService thePriceService) {
		avoidMeatService = theAvoidMeatService;
		priceService = thePriceService;
	}
	
	@GetMapping("/deleteAvoid")
	public String deleteAvoid(@RequestParam("id")int id, RedirectAttributes attr) {
		attr.addFlashAttribute("avoidMessage", avoidMeatService.findById(id).getType() + " deleted successfully.");
		avoidMeatService.deleteAvoidMeat(avoidMeatService.findById(id));
		
		for(Staff staff:staffService.getActiveStaffs(1)) {
			String meats = staff.getAvoidMeatIds();
			List<String> toRemove = new ArrayList<>();
			if(meats!=null) {
				List<String> ids = new ArrayList<>(Arrays.asList(meats.split(",")));
				for(String temp : ids) {
					if(temp.equals(String.valueOf(id))) {
						toRemove.add(temp);
					}
				}
			
				ids.removeAll(toRemove);
				String avoidMeatList = "";
				if (ids != null) {
					for (String avoidMeat : ids) {
						if (avoidMeatList.isEmpty() || avoidMeatList.isEmpty()) {
							avoidMeatList = avoidMeat;
						} else {
							avoidMeatList = avoidMeatList + "," + avoidMeat;
						}
					}

				}
				staff.setAvoidMeatIds(avoidMeatList);
				staffService.addStaff(staff);
			}
		}
		
		attr.addAttribute("scroll", "scroll");
		return "redirect:/menu";
	}
	
	@PostMapping("/change_price")
	public String changePrice(@RequestParam("priceList") int price_ID, RedirectAttributes redirectAttrsl) {

		Price price = priceService.findById(price_ID);
		List<Price> prices = priceService.findAll();
		for (Price tempPrice : prices) {
			tempPrice.setStatus((byte) 0);
		}
		price.setStatus((byte) 1);
		priceService.save(price);
		redirectAttrsl.addAttribute("totalPrice", price.getTotal_price());
		redirectAttrsl.addAttribute("datPrice", price.getDATprice());
		redirectAttrsl.addAttribute("staffPrice", price.getStaff_price());
		return "redirect:/menu";
	}

	@GetMapping("/menu")
	public String showMenu(Model theModel, Authentication authentication,
			@RequestParam(name = "totalPrice", required = false) Integer totalPrice,
			@RequestParam(name = "datPrice", required = false) Integer datPrice,
			@RequestParam(name = "staffPrice", required = false) Integer staffPrice) {
		try {
			String role = authentication.getAuthorities().toArray()[0].toString();
			if (role.equals("admin")) {
				
				AvoidMeat theAvoidMeat = new AvoidMeat();
				theModel.addAttribute("avoidmeat", theAvoidMeat);

				Price addPrice = new Price();
				theModel.addAttribute("addprice", addPrice);

				List<Price> priceList = priceService.findAll();
				theModel.addAttribute("priceList", priceList);

				List<AvoidMeat> avoidmeat = avoidMeatService.findAll();
				if (avoidmeat.isEmpty()) {
					theModel.addAttribute("avoidMessage", "Please add an option");
				} else {
					theModel.addAttribute("avoidmeats", avoidmeat);
				}
			
				Price activePrice = priceService.findActivePrice();
				if (activePrice == null) {
					theModel.addAttribute("priceMessage", "Please set a price");
				} else {
					Byte status = activePrice.getStatus();
					if (status != null || status.equals((byte)1)) {
						
						// perform actions when status is equal to myByteObject
						theModel.addAttribute("totalPrice", activePrice.getTotal_price());

						theModel.addAttribute("datPrice", activePrice.getDATprice());
						theModel.addAttribute("staffPrice", activePrice.getStaff_price());
					}
				}

				String pdfFileName = "currentweek.pdf";
				if (pdfFileName != null) {
					try {
						String encodedPdf = menuPdfService.getPdfAsByteString(pdfFileName);
						theModel.addAttribute("pdf", encodedPdf);

					} catch (NoSuchFileException e) {
						return "admin/menu";
					} catch (IOException e) {
						System.err.println("Error reading file: " + e.getMessage());
					}
				}
				
				String pdfFileName2 = "nextweek.pdf";
				if (pdfFileName2 != null) {
					try {
						String encodedPdf1 = menuPdfService.getPdfAsByteString(pdfFileName2);
						theModel.addAttribute("pdf1", encodedPdf1);
					} catch (NoSuchFileException e) {
						return "admin/menu";
					} catch (IOException e) {
						System.err.println("Error reading file: " + e.getMessage());
					}
				}
			theModel.addAttribute("datPrice", activePrice.getDATprice());
			theModel.addAttribute("staffPrice", activePrice.getStaff_price());
			
			}
			List<AvoidMeat> avoidMeats = avoidMeatService.findAll();
			String meatTypes = "";
			String staffCounts = "";
			for(AvoidMeat meat : avoidMeats) {
				List<Staff> staffs = staffService.getByAvoidMeatIds(String.valueOf(meat.getAvoidmeat_ID()));
				if(staffCounts=="") {
					staffCounts = String.valueOf(staffs.size());
				}else {
					staffCounts = staffCounts + "," + staffs.size();
				}
				
				if(meatTypes=="") {
					meatTypes = meat.getType();
				} else {
					meatTypes = meatTypes + "," + meat.getType();
				}
				System.out.println(meat.getType() + "==>" + staffs.size());
			}
			theModel.addAttribute("meatTypes", meatTypes);
			theModel.addAttribute("staffCounts", staffCounts);
			return "admin/menu";
			
		} 
		catch (NullPointerException e) {
			return "admin/menu";
		}
		
}
	
	@PostMapping("/add_price")
	public String savePrice(@ModelAttribute("addprice") Price thePrice, Authentication authentication, RedirectAttributes redirect) {
		List<Price> prices = priceService.findAll();
		for (Price tempPrice : prices) {
			tempPrice.setStatus((byte) 0);
		}
		
		Price existingDATPrice=priecPriceRepository.findUniquePrice(thePrice.getTotal_price(), thePrice.getDATprice());
		if(existingDATPrice !=null) {
			redirect.addFlashAttribute("unique", "Added Price already existed!");
		}
		else {
			int staff_price = thePrice.getTotal_price() - thePrice.getDATprice();
			thePrice.setStaff_price(staff_price);
			thePrice.setCreated_date(LocalDateTime.now());
			thePrice.setStatus((byte) 1);
			thePrice.setCreated_by(staffService.getStaffById(authentication.getName()).getName());
			priceService.save(thePrice);
		 } 
		return "redirect:/menu";
	}

	@PostMapping("/saveAvoidMeat")
	public String saveAvoidMeat(@ModelAttribute("avoidmeat") AvoidMeat theAvoidMeat,RedirectAttributes redirectAttrsl, BindingResult br,
			Model theModel) {

		AvoidMeat existingAvoidMeat = avoidMeatRepository.findByType(theAvoidMeat.getType());

		if (existingAvoidMeat != null) {
			redirectAttrsl.addFlashAttribute("avoidMeatMessage",theAvoidMeat.getType()+ " already existed!");
		}else {
		theAvoidMeat.setCreated_date(LocalDateTime.now());
		avoidMeatService.save(theAvoidMeat);
		redirectAttrsl.addFlashAttribute("successMessage", theAvoidMeat.getType() + " added successfully.");
		}
		redirectAttrsl.addAttribute("scroll", "scroll");
		return "redirect:/menu";
	}

	@PostMapping("/import_menu")
	public String uploadPdf(@RequestParam("pdfFile") MultipartFile pdfFile, Model model, RedirectAttributes redirectAttributes) throws IOException {


	/*public String uploadPdf(@RequestParam("pdfFile") MultipartFile pdfFile, Model model) throws IOException {*/

		String message = "";
		try {
			// Get the filename of the PDF file
			String fileName = StringUtils.cleanPath(pdfFile.getOriginalFilename());
			String pdfOldFileName = new File(fileName).getName();
			String pdfNewFileName = "currentweek.pdf";

			// Create a Path object for the resource directory
			Path resourceDirectory = Paths.get("src", "main", "resources", "pdfs");

			// Create the directory if it doesn't exist
			if (!Files.exists(resourceDirectory)) {
				Files.createDirectories(resourceDirectory);
			}

			// Create a Path object for the PDF file
			Path pdfPath = resourceDirectory.resolve(pdfNewFileName);
			if (pdfFile.getBytes().length == 0) {
				message = "Failed to upload file: the file is empty.";

			} else {
				// Write the PDF file to the resource directory
				Files.write(pdfPath, pdfFile.getBytes());
				redirectAttributes.addFlashAttribute("currentweek" , "File successfully uploaded!");
				message = "File uploaded successfully: " + pdfNewFileName;

			}

		} catch (IOException e) {
			message = "Failed to upload file: " + e.getMessage();
		}
		model.addAttribute("message", message);
		// retrive pdf from resources folder

		return "redirect:/menu";
	}

	@PostMapping("/import_menu2")
	public String uploadPdf1(@RequestParam("pdfFile2") MultipartFile pdfFile2, Model model, RedirectAttributes redirectAttributes) throws IOException {

		String message = "";
		try {
			// Get the filename of the PDF file
			String fileName = StringUtils.cleanPath(pdfFile2.getOriginalFilename());

			String pdfOldFileName = new File(fileName).getName();

			String pdfNewFileName = "nextweek.pdf";

			// Create a Path object for the resource directory
			Path resourceDirectory = Paths.get("src", "main", "resources", "pdfs");

			// Create the directory if it doesn't exist
			if (!Files.exists(resourceDirectory)) {
				Files.createDirectories(resourceDirectory);
			}

			// Create a Path object for the PDF file
			Path pdfPath = resourceDirectory.resolve(pdfNewFileName);

			if (pdfFile2.getBytes().length == 0) {
				message = "Failed to upload file: the file is empty.";

			} else {
				// Write the PDF file to the resource directory
				Files.write(pdfPath, pdfFile2.getBytes());
				redirectAttributes.addFlashAttribute("nextweek" , "File successfully uploaded!");
				message = "File uploaded successfully: " + pdfNewFileName;
			}

		} catch (IOException e) {
			message = "Failed to upload file: " + e.getMessage();
		}
		model.addAttribute("message2", message);

		// retrive pdf from resources folder

		return "redirect:/menu";
	}

}