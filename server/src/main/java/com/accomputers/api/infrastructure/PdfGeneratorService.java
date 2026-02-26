package com.accomputers.api.infrastructure;

import java.io.ByteArrayOutputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.itextpdf.html2pdf.HtmlConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// Application
import com.accomputers.api.application.ports.output.PdfGeneratorInterface;
import com.accomputers.api.application.dtos.PageDTO;
import com.accomputers.api.application.dtos.ProductCriteria;
import com.accomputers.api.application.ports.output.LoggerPort;
import com.accomputers.api.application.ports.output.repositories.ProductRepositoryInterface;

// Domain
import com.accomputers.api.domain.entities.Product;

@Component
public class PdfGeneratorService implements PdfGeneratorInterface {

    private final LoggerPort logger;
    private final ProductRepositoryInterface productRepository;

    @Autowired
    public PdfGeneratorService(LoggerPort logger, ProductRepositoryInterface productRepository) {
        this.logger = logger;
        this.productRepository = productRepository;
    }

    @Override
    public ByteArrayOutputStream generateProductCatalog(Integer categoryId) {
        try {
            PageDTO<Product> products = productRepository
                    .findAll(new ProductCriteria(1, 1000, null, categoryId, null, null, null, null, null, null, null));

            String htmlContent = buildProductCatalogHtml(products);
            return generatePdfFromHtml(htmlContent);
        } catch (Exception e) {
            logger.error("Error generating product catalog PDF: " + e.getMessage(), e);
            throw new RuntimeException("Failed to generate product catalog PDF: " + e.getMessage(), e);
        }
    }

    private String buildProductCatalogHtml(PageDTO<Product> products) {
        List<Product> computers = new ArrayList<>();
        List<Product> components = new ArrayList<>();

        for (Product product : products.data()) {
            ProductGroup group = classifyProductGroup(product);
            if (group == ProductGroup.COMPUTERS) {
                computers.add(product);
            } else {
                components.add(product);
            }
        }

        String appDomain = getAppDomain();

        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html lang=\"es\">");
        html.append("<head>");
        html.append("<meta charset=\"UTF-8\">");
        html.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
        html.append("<title>Reporte de Productos</title>");
        html.append("<style>");
        html.append("@page { margin: 0; }");
        html.append("</style>");
        html.append("</head>");
        html.append("<body style=\"background-color: #111827; color: white; font-family: system-ui; margin: 0; padding: 2rem;\">");

        html.append("<div style=\"text-align: center;\">");
        html.append("<h1 style=\"margin: 0; font-size: 32px; font-weight: 800;\">");
        html.append("<span style=\"color: white;\">AC</span>");
        html.append("<span style=\"font-style: italic; color: #4e99d3;\">COMPUTERS</span>");
        html.append("</h1>");
        html.append("<p style=\"margin: 5px 0;\">Punto de venta: Centro Comercial Alta Tecnología</p>");
        html.append("<p style=\"margin: 3px 0;\">Cra. 15 No 77-05 Local __ primer piso</p>");
        html.append("<p style=\"margin: 3px 0;\">Móvil: 311 8835868</p>");
        html.append("<p style=\"margin: 3px 0;\">amaliacastro78@gmail.com</p>");
        html.append("</div>");

        if (!computers.isEmpty()) {
            html.append(buildProductsTableSection("Computadores", computers, appDomain));
        }

        if (!components.isEmpty()) {
            html.append(buildProductsTableSection("Componentes", components, appDomain));
        }

        html.append("</body>");
        html.append("</html>");

        return html.toString();
    }

    private String buildProductsTableSection(String title, List<Product> products, String appDomain) {
        StringBuilder html = new StringBuilder();
        html.append("<h2>").append(escapeHtml(title)).append("</h2>");
        html.append("<table style=\"width: 100%; border-collapse: collapse; margin-top: 20px;\">");
        html.append("<tr style=\"background-color: #4e99d3; color: #020617; border-radius: 5px;\">");
        html.append("<th style=\"text-align: left;\">Producto</th>");
        html.append("<th style=\"text-align: left;\">Precio</th>");
        html.append("</tr>");

        for (int idx = 0; idx < products.size(); idx++) {
            Product p = products.get(idx);
            String paddingTop = idx == 0 ? "10px" : "0";
            String label = escapeHtml(p.getId() + " - " + p.getName());

            html.append("<tr>");
            html.append("<td style=\"width: 80%; padding-top: ").append(paddingTop).append("\">");

            if (appDomain != null && !appDomain.isBlank()) {
                String href = escapeHtml(appDomain + "/product/" + p.getId());
                html.append("<a style=\"color: #4e99d3;\" href=\"").append(href).append("\">").append(label).append("</a>");
            } else {
                html.append("<span style=\"color: #4e99d3;\">").append(label).append("</span>");
            }

            html.append("</td>");
            html.append("<td style=\"width: 20%; padding-top: ").append(paddingTop).append("\">");
            html.append("COP ").append(escapeHtml(formatCop(p)));
            html.append("</td>");
            html.append("</tr>");
        }

        html.append("</table>");
        return html.toString();
    }

    private String formatCop(Product product) {
        float discounted = product.getPrice().applyDiscount(product.getDiscount()).getValue();
        long truncated = (long) Math.floor(discounted);

        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.forLanguageTag("es-CO"));
        numberFormat.setMaximumFractionDigits(0);
        numberFormat.setMinimumFractionDigits(0);
        return numberFormat.format(truncated);
    }

    private String getAppDomain() {
        String appDomain = System.getenv("APP_DOMAIN");
        if (appDomain == null || appDomain.isBlank()) {
            appDomain = System.getenv("APP_URL");
        }
        if (appDomain == null) {
            return null;
        }
        return appDomain.endsWith("/") ? appDomain.substring(0, appDomain.length() - 1) : appDomain;
    }

    private enum ProductGroup {
        COMPUTERS,
        COMPONENTS
    }

    private ProductGroup classifyProductGroup(Product product) {
        if (product == null || product.getSubCategory() == null) {
            return ProductGroup.COMPONENTS;
        }

        Integer categoryId = product.getSubCategory().getCategoryId();
        if (categoryId != null) {
            if (categoryId == 1) {
                return ProductGroup.COMPUTERS;
            }
            if (categoryId == 2) {
                return ProductGroup.COMPONENTS;
            }
        }

        String name = product.getSubCategory().getName();
        String slug = product.getSubCategory().getSlug() != null ? product.getSubCategory().getSlug().getValue() : null;
        String haystack = (name == null ? "" : name) + " " + (slug == null ? "" : slug);
        haystack = haystack.toLowerCase();

        if (haystack.contains("comput") || haystack.contains("pc") || haystack.contains("laptop")
                || haystack.contains("portatil") || haystack.contains("portátil")) {
            return ProductGroup.COMPUTERS;
        }

        return ProductGroup.COMPONENTS;
    }

    private ByteArrayOutputStream generatePdfFromHtml(String htmlContent) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        HtmlConverter.convertToPdf(htmlContent, outputStream);
        return outputStream;
    }

    private String escapeHtml(String input) {
        if (input == null)
            return "";
        return input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;");
    }
}
