package com.dat.CateringService.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.dat.CateringService.entity.AvoidMeat;
import com.dat.CateringService.entity.Price;
import com.dat.CateringService.service.AvoidMeatService;
import com.dat.CateringService.service.MenuPdfService;
import com.dat.CateringService.service.PriceService;

@Controller
public class MenuController {

	@Autowired
	private MenuPdfService menuPdfService;

	private AvoidMeatService avoidMeatService;
	private PriceService priceService;

	public MenuController(AvoidMeatService theAvoidMeatService, PriceService thePriceService) {
		avoidMeatService = theAvoidMeatService;
		priceService = thePriceService;
	}

	@Autowired
	private AvoidMeatRepository avoidMeatRepository;

	@PostMapping("/change_price")
	public String changePrice(@RequestParam("priceList") int price_ID, RedirectAttributes redirectAttrsl) {

		Price price = priceService.findById(price_ID);
		List<Price> prices = priceService.findAll();
		for (Price tempPrice : prices) {
			tempPrice.setStatus((byte) 0);
		}
		price.setStatus((byte) 1);
		priceService.save(price);
		/*
		 * if(status !=null) { price.setStatus(status); } priceService.save(price);
		 */

		redirectAttrsl.addAttribute("totalPrice", price.getTotal_price());
		/*
		 * System.out.println(price.getTotal_price());
		 */
		redirectAttrsl.addAttribute("datPrice", price.getDAT_price());
		// System.out.println(price.getDAT_price());

		redirectAttrsl.addAttribute("staffPrice", price.getStaff_price());
		// System.out.println(price.getStaff_price());

		return "redirect:/menu";
	}

	@GetMapping("/menu")
	public String showMenu(Model theModel, @RequestParam(name = "totalPrice", required = false) Integer totalPrice,
			@RequestParam(name = "datPrice", required = false) Integer datPrice,
			@RequestParam(name = "staffPrice", required = false) Integer staffPrice) {

		AvoidMeat theAvoidMeat = new AvoidMeat();
		theModel.addAttribute("avoidmeat", theAvoidMeat);

		Price addPrice = new Price();
		theModel.addAttribute("addprice", addPrice);

		List<Price> priceList = priceService.findAll();
		theModel.addAttribute("priceList", priceList);

		List<AvoidMeat> avoidmeat = avoidMeatService.findAll();
		theModel.addAttribute("avoidmeats", avoidmeat);

		/* Price activePrice = priceService.findActivePrice(); */
		Price activePrice = priceService.findActivePrice();
		System.out.println();
		Byte status = activePrice.getStatus();

		if (status != null || status.equals(1)) {
			// perform actions when status is equal to myByteObject
			theModel.addAttribute("totalPrice", activePrice.getTotal_price());

			theModel.addAttribute("datPrice", activePrice.getDAT_price());
			theModel.addAttribute("staffPrice", activePrice.getStaff_price());
		}
		// Set the default values if the status is active
		/*
		 * else {
		 * 
		 * if (totalPrice != null) { theModel.addAttribute("totalPrice", totalPrice); }
		 * 
		 * if (datPrice != null) { theModel.addAttribute("datPrice", datPrice); }
		 * 
		 * if (staffPrice != null) { theModel.addAttribute("staffPrice", staffPrice); }
		 * 
		 * }
		 */
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
		return "admin/menu";
	}

	@PostMapping("/add_price")
	public String savePrice(@ModelAttribute("addprice") Price thePrice) {

		int staff_price = thePrice.getTotal_price() - thePrice.getDAT_price();
		thePrice.setStaff_price(staff_price);
		thePrice.setCreated_date(LocalDateTime.now());
		thePrice.setStatus((byte) 0);
		thePrice.setCreated_by("Mg Mg");

		priceService.save(thePrice);
		return "redirect:/menu";
	}

	@PostMapping("/saveAvoidMeat")
	public String saveAvoidMeat(@ModelAttribute("avoidmeat") @Valid AvoidMeat theAvoidMeat, BindingResult br,
			Model theModel) {

		if (br.hasErrors()) {
			System.out.println(br);
			return "redirect:/menu";
		}

		AvoidMeat existingAvoidMeat = avoidMeatRepository.findByType(theAvoidMeat.getType());
		System.out.println(existingAvoidMeat);

		if (existingAvoidMeat != null) {
			theModel.addAttribute("message", "Nani nani is already exist.");
		}

		theAvoidMeat.setCreated_date(LocalDateTime.now());
		avoidMeatService.save(theAvoidMeat);
		return "redirect:/menu";

	}

	@PostMapping("/import_menu")
	public String uploadPdf(@RequestParam("pdfFile") MultipartFile pdfFile, Model model) throws IOException {
		/*
		 * AvoidMeat theAvoidMeat = new AvoidMeat(); model.addAttribute("avoidmeat",
		 * theAvoidMeat);
		 */

		/*
		 * String pdfFileName = "currentweek.pdf"; String encodedPdf =
		 * menuPdfService.getPdfAsByteString(pdfFileName); model.addAttribute("pdf",
		 * encodedPdf);
		 */
		String message = "";
		try {
			// Get the filename of the PDF file
			String fileName = StringUtils.cleanPath(pdfFile.getOriginalFilename());

			String pdfOldFileName = new File(fileName).getName();

			String pdfNewFileName = "currentweek.pdf";

			/* System.out.println(fileName); */
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

				message = "File uploaded successfully: " + pdfNewFileName;
				/* System.out.println(message); */
			}

		} catch (IOException e) {
			message = "Failed to upload file: " + e.getMessage();
		}
		model.addAttribute("message", message);

		// retrive pdf from resources folder

		return "redirect:/menu";
	}

	@PostMapping("/import_menu2")
	public String uploadPdf1(@RequestParam("pdfFile2") MultipartFile pdfFile2, Model model) throws IOException {

		/*
		 * AvoidMeat theAvoidMeat = new AvoidMeat(); model.addAttribute("avoidmeat",
		 * theAvoidMeat);
		 */

		/*
		 * String pdfFileName = "currentweek.pdf"; String encodedPdf =
		 * menuPdfService.getPdfAsByteString(pdfFileName); model.addAttribute("pdf",
		 * encodedPdf);
		 */

		String message = "";
		try {
			// Get the filename of the PDF file
			String fileName = StringUtils.cleanPath(pdfFile2.getOriginalFilename());

			String pdfOldFileName = new File(fileName).getName();

			String pdfNewFileName = "nextweek.pdf";

			/* System.out.println(fileName); */
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
