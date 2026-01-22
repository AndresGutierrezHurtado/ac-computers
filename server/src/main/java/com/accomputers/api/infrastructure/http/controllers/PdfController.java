package com.accomputers.api.infrastructure.http.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accomputers.api.application.ports.output.PdfGeneratorInterface;

import java.io.ByteArrayOutputStream;

/**
 * Controller for PDF generation operations
 */
@RestController
@RequestMapping("/pdf")
public class PdfController {

    private final PdfGeneratorInterface pdfGenerator;

    @Autowired
    public PdfController(PdfGeneratorInterface pdfGenerator) {
        this.pdfGenerator = pdfGenerator;
    }

    /**
     * Generates a product catalog PDF
     * 
     * @return ResponseEntity containing the generated PDF
     */
    @PostMapping("/generate")
    public ResponseEntity<byte[]> generateProductCatalog() {
        try {
            ByteArrayOutputStream pdfStream = pdfGenerator.generateProductCatalog();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "product-catalog.pdf");
            headers.setContentLength(pdfStream.size());

            return new ResponseEntity<>(pdfStream.toByteArray(), headers, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}