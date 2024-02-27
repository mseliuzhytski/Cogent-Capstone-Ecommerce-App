package com.cogent.ecommerce.controller;

import com.cogent.ecommerce.model.Product;
import com.cogent.ecommerce.repository.ProductRepository;
import com.cogent.ecommerce.service.BulkUploadService;
import com.cogent.ecommerce.service.FilenameObject;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BulkUploadService bulkUploadService;

    @GetMapping(value="/product/list")
    public List<Product> getProducts() {
        return productRepository.getAllProducts();
    }

    @GetMapping(value="/product/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable int id) {
        Product p = null;
        try {
            p = productRepository.getById(id);
        } catch (DataIntegrityViolationException ex) {
        }
        if (p != null) {
            return ResponseEntity.status(HttpStatus.OK).body(p);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping(value="/product")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product p = null;
        try {
            p = productRepository.saveProduct(product);
        } catch (DataIntegrityViolationException ex) {
        }
        if (p != null) {
            return ResponseEntity.status(HttpStatus.OK).body(p);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping(value="/product/{id}")
    public ResponseEntity<Product> deleteProductById(@PathVariable int id) {
        Product p = null;
        try {
            p = productRepository.deleteProductById(id);
        } catch (DataIntegrityViolationException ex) {
            ex.printStackTrace();
        }
        if (p != null) {
            return ResponseEntity.status(HttpStatus.OK).body(p);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping(value="/product/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable int id,
                                                 @RequestBody Product newProduct) {
        Product p = null;
        try {
            p = productRepository.updateProduct(newProduct, id);
        } catch (DataIntegrityViolationException ex) {
        }
        if (p != null) {
            return ResponseEntity.status(HttpStatus.OK).body(p);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * https://howtodoinjava.com/spring-boot/spring-boot-file-upload-rest-api/
     */

    public final static String UPLOAD_FOLDER = "Files-Upload";

    public final static String IMAGES_FOLDER = "public/";


    public static String saveFile(String fileName, MultipartFile multipartFile, String folder)
            throws IOException {
        Path uploadPath = Paths.get(folder);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        String fileCode = RandomStringUtils.randomAlphanumeric(8);
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileCode + "-" + fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save file: " + fileName, ioe);
        }
        String filename = folder + "/" + fileCode + "-" + fileName;
        return filename;
    }

    @PostMapping("/upload-image")
    public ResponseEntity uploadImage(@RequestParam("file") MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        FilenameObject fObject = new FilenameObject();

        try {
            String completeFilename = saveFile(fileName, file, IMAGES_FOLDER);
            String[] splits = completeFilename.split("//");
            String abbreviatedFilename = splits[splits.length-1];
            fObject.setFilename(completeFilename);
            fObject.setAbbreviatedFilename(abbreviatedFilename);
            return ResponseEntity.ok(fObject);
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Could not save filename " + fileName);
        }
    }

    @PostMapping("/bulk-upload")
    public ResponseEntity<Map<String, String>> csvUploadFile(
            @RequestParam("file") MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        long size = file.getSize();

        String excelFile = saveFile(fileName, file, UPLOAD_FOLDER);

        String errorMessage = "";
        try {
            bulkUploadService.bulkUpload(excelFile);
        } catch (BulkUploadService.CsvParseException e) {
            System.out.println("--- Could not perform bulk upload ---");
            System.err.println(e.getMessage());
            errorMessage = e.getMessage();
        }

        Map<String, String> map = new HashMap<>();

        // Populate the map with file details
        map.put("fileName", file.getOriginalFilename());
        map.put("fileSize", file.getSize() + "");
        map.put("fileContentType", file.getContentType());
        map.put("downloadUri", excelFile);
        if (!errorMessage.equals("")) {
            map.put("errorMessage", errorMessage);
        }
        if (errorMessage.equals("")) {
            return ResponseEntity.ok(map);
        } else {
            return ResponseEntity.badRequest().body(map);
        }
    }

}
