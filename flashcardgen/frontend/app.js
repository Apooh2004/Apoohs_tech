// Handle the upload form submission
document.getElementById("upload-form").addEventListener("submit", async function (e) {
  e.preventDefault();
  
  const fileInput = document.getElementById("pdf-file");
  const file = fileInput.files[0];

  if (!file) {
    alert("Please select a file.");
    return;
  }

  // Simulate sending the file to the backend and receiving flashcards
  const flashcards = await simulateBackend(file);

  // If no flashcards are found
  if (flashcards.length === 0) {
    document.getElementById("message").style.display = "block";
    document.getElementById("message").textContent = "No flashcards found.";
    document.getElementById("flashcard-section").style.display = "none";
    return;
  }

  // Display the flashcards
  document.getElementById("message").style.display = "none";
  document.getElementById("flashcard-section").style.display = "block";
  const flashcardContainer = document.getElementById("flashcard-container");
  flashcardContainer.innerHTML = ""; // Clear previous flashcards

  flashcards.forEach((card) => {
    const flashcard = document.createElement("div");
    flashcard.classList.add("flashcard");
    flashcard.textContent = card.question;

    // Flip functionality
    flashcard.addEventListener("click", () => {
      if (flashcard.classList.contains("flipped")) {
        flashcard.textContent = card.question;
        flashcard.classList.remove("flipped");
      } else {
        flashcard.textContent = card.answer;
        flashcard.classList.add("flipped");
      }
    });

    flashcardContainer.appendChild(flashcard);
  });
});

// Simulate backend logic
async function simulateBackend(file) {
  // This is where you'd integrate the backend logic.
  // For now, it just returns a mock set of flashcards.
  console.log("Processing file:", file.name);

  return new Promise((resolve) => {
    setTimeout(() => {
      resolve([
        { question: "What is Java?", answer: "A programming language." },
        { question: "What is a Class?", answer: "A blueprint for objects." },
        { question: "What is Inheritance?", answer: "A way to reuse code." }
      ]);
    }, 1000);
  });
}
