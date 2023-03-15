package com.dat.CateringService.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

import org.springframework.stereotype.Service;

@Service
public class MenuPdfService {

	public String getPdfAsByteString(String fileName) throws IOException {
		// Create a Path object for the resource directory
		Path resourceDirectory = Paths.get("src", "main", "resources", "pdfs");

		// Create a Path object for the PDF file
		Path pdfPath = resourceDirectory.resolve(fileName);

		// Read the contents of the PDF file into a byte array
		byte[] fileData = Files.readAllBytes(pdfPath);

		// Encode the byte array using Base64 encoding
		String byteString = Base64.getEncoder().encodeToString(fileData);

		return byteString;

	}
}
