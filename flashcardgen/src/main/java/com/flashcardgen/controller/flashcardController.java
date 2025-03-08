package com.flashcardgen.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.flashcardgen.model.FlashCardmodel;
import com.flashcardgen.service.FlashCardService;

@RestController
@CrossOrigin(origins = "http://localhost:5500") // Allow requests from frontend 
@RequestMapping("/api")
public class flashcardController {
    private static final Logger logger = LoggerFactory.getLogger(flashcardController.class);

    @Autowired
    private FlashCardService flashCardService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadPdf(@RequestParam("file") MultipartFile file) {
        logger.info("Received file: {} with size: {}", file.getOriginalFilename(), file.getSize());

        // Check if the file is empty
        if (file.isEmpty()) {
            logger.warn("Uploaded file is empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty");
        }

        try {
            List<FlashCardmodel> flashcards = flashCardService.generateFlashcards(file);
            logger.info("Successfully generated {} flashcards", flashcards.size());
            return ResponseEntity.ok(flashcards);
        } catch (Exception e) {
            logger.error("Error occurred while uploading file", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing file: " + e.getMessage());
        }
    }
}