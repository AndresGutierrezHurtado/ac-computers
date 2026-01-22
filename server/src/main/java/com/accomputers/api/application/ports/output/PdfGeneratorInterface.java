package com.accomputers.api.application.ports.output;

import java.io.ByteArrayOutputStream;

/**
 * Port for PDF generation operations following hexagonal architecture.
 * This is an output port that defines the contract for PDF generation
 * functionality
 * that the application layer requires from the infrastructure layer.
 */
public interface PdfGeneratorInterface {
    /**
     * Generates a product catalog PDF
     * 
     * @param productData the product data to include in the catalog
     * @return ByteArrayOutputStream containing the generated PDF
     */
    ByteArrayOutputStream generateProductCatalog();
}