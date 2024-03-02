package com.cogent.ecommerce.service;

import com.cogent.ecommerce.model.Category;
import com.cogent.ecommerce.model.Product;
import com.cogent.ecommerce.repository.ProductRepository;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.Instant;
import java.util.*;

@Service
public class BulkUploadService {

    public static final int COL_NAME = 0;
    public static final int COL_PRICE = 1;
    public static final int COL_STOCK = 2;
    public static final int COL_CATEGORY = 3;
    public static final int COL_IMAGE_LOCATION = 4;
    public static final int COL_DETAILS = 5;

    public static final String COMMA_DELIMITER = ",";

    @Autowired
    private ProductRepository repository;

    @Autowired
    private ProductService productService;

    public static class ProductCategoryDTO {
        private Product product;
        private String categoryName;
    }

    public void bulkUpload(String excelFile) throws CsvParseException {
        List<ProductCategoryDTO> products = bulkUploadGetProducts(excelFile);
        for (ProductCategoryDTO product : products) {
            repository.saveProduct(product.product);
            productService.addCategoryToProduct(product.product, product.categoryName);
        }
    }

    public List<ProductCategoryDTO> bulkUploadGetProducts(String excelFile) throws CsvParseException {
        XSSFWorkbook workbook = null;
        try {
            workbook = new XSSFWorkbook(new File(excelFile));
            XSSFSheet userSheet = workbook.getSheetAt(0);
            List<ProductCategoryDTO> products = parseProducts(userSheet);
            return products;
        } catch (IOException e) {
            throw new CsvParseException(e.getMessage());
        } catch (InvalidFormatException e) {
            throw new CsvParseException(e.getMessage());
        }
    }

    public Double getDouble(XSSFCell cell) throws CsvParseException {
        Double number = 0.0;
        if (cell == null) {
            return 0.0;
        }
        try {
            number = cell.getNumericCellValue();
            return number.doubleValue();
        } catch (NumberFormatException ex) {
            throw new CsvParseException("Could not parse the price " + cell.getStringCellValue()
                    + " as a double");
        }
    }

    public Integer getInteger(XSSFCell cell) throws CsvParseException {
        Double number = 0.0;
        if (cell == null) {
            return 0;
        }
        try {
            number = cell.getNumericCellValue();
            return number.intValue();
        } catch (NumberFormatException ex) {
            throw new CsvParseException("Could not parse the price " + cell.getNumericCellValue()
              + " as an integer");
        }
    }

    public List<ProductCategoryDTO> parseProducts(XSSFSheet sheet) throws CsvParseException {
        List<ProductCategoryDTO> products = new LinkedList<>();
        int rowNumber = 1;
        while (true) {
            XSSFRow row = sheet.getRow(rowNumber);
            if (row == null) {
                break;
            }
            XSSFCell cell = row.getCell(COL_NAME);
            if (cell == null || cell.getStringCellValue() == null || cell.getStringCellValue().equals("")) {
                break;
            }
            String name = cell.getStringCellValue();
            double price = getDouble(row.getCell(COL_PRICE));
            int stock = getInteger(row.getCell(COL_STOCK));
            String category = row.getCell(COL_CATEGORY).getStringCellValue();
            String imageLocation = row.getCell(COL_IMAGE_LOCATION).getStringCellValue();
            String details = row.getCell(COL_DETAILS).getStringCellValue();
            Product product = new Product();
            product.setName(name);
            product.setPrice(price);
            product.setStock(stock);
//            product.setCategory(category);
           // product.setCategory(category);

            product.setImageLocation(imageLocation);
            product.setDetails(details);
            product.setDateAdded(Instant.now().toEpochMilli());

            ProductCategoryDTO dto = new ProductCategoryDTO();
            dto.product = product;
            dto.categoryName = category;
            products.add(dto);
            rowNumber++;
        }
        return products;
    }

    public static class CsvParseException extends Exception {
        public CsvParseException(String name) {
            super(name);
        }
    }

}
