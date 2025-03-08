package com.flashcardgen.flashcardgen;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FlashcardgenApplication {

	public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the path of the PDF file to upload:");

        String filePath = scanner.nextLine();
        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("File does not exist. Please check the path and try again.");
            scanner.close();
            return;
        }

        try {
            List<FlashCard> flashCards = createFlashCardsFromPdf(file);
            if (flashCards.isEmpty()) {
                System.out.println("No flashcards found.");
                scanner.close();
                return;
            }

            // To pick questions randomly
            Collections.shuffle(flashCards);

            // Start the flashcards 
            startFlashCard(flashCards, scanner);
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    private static List<FlashCard> createFlashCardsFromPdf(File file) throws IOException {
        List<FlashCard> flashCards = new ArrayList<>();
        try (PDDocument document = PDDocument.load(file)) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(document);

            // Split the text into lines
            String[] lines = text.split("\n");
            StringBuilder question = new StringBuilder();
            StringBuilder answer = new StringBuilder();
            boolean isQuestion = true;

            for (String line : lines) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue; // Skip empty lines
                }
                if (line.startsWith("Q:") || line.matches("^\\d+\\).*")) {
                    // Save the previous question-answer pair
                    if (question.length() > 0 && answer.length() > 0) {
                        flashCards.add(new FlashCard(question.toString().trim(), answer.toString().trim()));
                        question.setLength(0);
                        answer.setLength(0);
                    }
                    // Start a new question
                    question.append(line.replaceFirst("^Q:\\s*", "").replaceFirst("^\\d+\\)\\s*", ""));
                    isQuestion = true;
                } else if (line.startsWith("A:")) {
                    // Start the answer
                    answer.append(line.replaceFirst("^A:\\s*", ""));
                    isQuestion = false;
                } else {
                    // Append to the current question or answer
                    if (isQuestion) {
                        question.append(" ").append(line);
                    } else {
                        answer.append(" ").append(line);
                    }
                }
            }

            // Add the last flashcard
            if (question.length() > 0 && answer.length() > 0) {
                flashCards.add(new FlashCard(question.toString().trim(), answer.toString().trim()));
            }
        }
        return flashCards;
    }

    private static void startFlashCard(List<FlashCard> flashCards, Scanner scanner) {
        for (FlashCard card : flashCards) {
            System.out.println("Question: " + card.getQuestion());
            System.out.println("Press Enter to reveal the answer");
            scanner.nextLine(); // Wait for user to press Enter
            System.out.println("Answer: " + card.getAnswer());
            System.out.println("Press Enter for the next question");
            scanner.nextLine(); // Wait for user to press Enter
        }
        System.out.println("End of flashcards.");
    }

    static class FlashCard {
        private final String question;
        private final String answer;

        public FlashCard(String question, String answer) {
            this.question = question;
            this.answer = answer;
        }

        public String getQuestion() {
            return question;
        }

        public String getAnswer() {
            return answer;
        }
    }
}
