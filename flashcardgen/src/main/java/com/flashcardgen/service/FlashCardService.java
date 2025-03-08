package com.flashcardgen.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.flashcardgen.model.FlashCardmodel;

public interface FlashCardService {
    List<FlashCardmodel> generateFlashcards(MultipartFile file);
}
