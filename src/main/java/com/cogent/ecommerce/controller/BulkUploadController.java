package com.cogent.ecommerce.controller;

import com.cogent.ecommerce.loader.DatabaseLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.crypto.Data;

@RestController
@RequestMapping("/databaseloader")
public class BulkUploadController {

    @Autowired
    private DatabaseLoader loader;

    @PostMapping
    public ResponseEntity loadDatabase() {
        try {
            loader.loadSeededData();
            return ResponseEntity.ok().build();
        } catch (DataAccessException ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
