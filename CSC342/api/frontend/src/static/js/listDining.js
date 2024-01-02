//Add event listeners for when the dining hall images are clicked
const imageDH1 = document.getElementById("diningHall1");
const imageDH2 = document.getElementById("diningHall2");
const imageDH3 = document.getElementById("diningHall3");

// Currently all three images redirect to the dining hall page with 
// the same information, but later they may use the API to have it render the relevant information
imageDH1.addEventListener("click", (e) => {
    window.location.href = "oval.html";
});

imageDH2.addEventListener("click", (e) => {
    window.location.href = "fountain.html";
});

imageDH3.addEventListener("click", (e) => {
    window.location.href = "clark.html";
});
