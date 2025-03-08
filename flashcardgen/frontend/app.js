// Handle the upload form submission
document.getElementById("upload-form").addEventListener("submit", async function (e) {
    e.preventDefault(); // Prevent the default form submission behavior
  
    // Get the selected file from the input
    const fileInput = document.getElementById("pdf-file").files[0];
  
    // Check if a file is selected
    if (!fileInput) {
      alert("Please select a file.");
      return;
    }
  
    // Create a FormData object to send the file
    const formData = new FormData();
    formData.append("file", fileInput);
  
    try {
      // Send the file to the backend using fetch
      const response = await fetch("http://localhost:8080/api/upload", {
        method: "POST",
        body: formData,
      });
  
      // Check if the response is OK (status code 200-299)
      if (!response.ok) {
        const errorText = await response.text(); // Get the error message from the response
        throw new Error(`Failed to upload file: ${errorText}`);
      }
  
      // Parse the JSON response (list of flashcards)
      const flashcards = await response.json();
  
      // If no flashcards are found
      if (flashcards.length === 0) {
        document.getElementById("message").style.display = "block";
        document.getElementById("message").textContent = "No flashcards found.";
        document.getElementById("flashcard-section").style.display = "none";
      } else {
        // Display flashcards
        document.getElementById("message").style.display = "none";
        document.getElementById("flashcard-section").style.display = "block";
        displayFlashcards(flashcards); // Call the function to display flashcards
      }
    } catch (error) {
      console.error("Error:", error);
      alert(`An error occurred while uploading the file: ${error.message}`);
    }
  });
  
  // Function to display flashcards
  function displayFlashcards(flashcards) {
    const flashcardsContainer = document.getElementById("flashcard-container");
    flashcardsContainer.innerHTML = ""; // Clear previous flashcards
  
    // Loop through each flashcard and create a new element for it
    flashcards.forEach((flashcard) => {
      const flashcardElement = document.createElement("div");
      flashcardElement.className = "flashcard";
  
      // Add question and answer to the flashcard
      flashcardElement.innerHTML = `
        <p><strong>Question:</strong> ${flashcard.question}</p>
        <p><strong>Answer:</strong> <span class="answer" style="display: none;">${flashcard.answer}</span></p>
        <button class="show-answer-btn">Show Answer</button>
      `;
  
      // Add event listener to the "Show Answer" button
      const showAnswerButton = flashcardElement.querySelector(".show-answer-btn");
      const answerElement = flashcardElement.querySelector(".answer");
  
      showAnswerButton.addEventListener("click", () => {
        // Toggle the visibility of the answer
        if (answerElement.style.display === "none") {
          answerElement.style.display = "inline";
          showAnswerButton.textContent = "Hide Answer";
        } else {
          answerElement.style.display = "none";
          showAnswerButton.textContent = "Show Answer";
        }
      });
  
      // Append the flashcard to the container
      flashcardsContainer.appendChild(flashcardElement);
    });
  }