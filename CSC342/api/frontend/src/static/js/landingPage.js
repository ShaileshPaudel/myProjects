import api from './APIClient.js';

//Redirect if already logged in
api.getCurrentUser().then(user => {
    //If got some user then this page shouldn't be visible, redirect
    document.location = '/stats.html';
}).catch(error => {
    //Otherwise do nothing
});

const defaultColor = "#ffffff";
const selectedColor = "#808080";

const buttonAbout1 = document.getElementById("showAbout1");
const buttonAbout2 = document.getElementById("showAbout2");

const aboutP1 = document.getElementById("aboutP1");
const aboutP2 = document.getElementById("aboutP2");

// Default display already be configured thanks to stats.css, but just in case set it again 
// so we can base the button coloration off of it
aboutP1.style.display = "block";
aboutP2.style.display = "none";

//Set button colored to indicate visible paragraph
buttonAbout1.style.background = selectedColor;

// Manages changing display in the about section 
buttonAbout1.addEventListener("click", (e) => {
    //If the paragraph is not currently showing
    if (aboutP1.style.display == "none") {
        // Make the paragraph we want visible
        aboutP2.style.display = "none";
        aboutP1.style.display = "block";

        // Update buttons
        buttonAbout1.style.backgroundColor = selectedColor;
        buttonAbout2.style.backgroundColor = defaultColor;
    }
});

buttonAbout2.addEventListener("click", (e) => {
    //If the paragraph is not currently showing
    if (aboutP2.style.display == "none") {
        // Make the paragraph we want visible
        aboutP1.style.display = "none";
        aboutP2.style.display = "block";

        // Update buttons
        buttonAbout1.style.backgroundColor = defaultColor;
        buttonAbout2.style.backgroundColor = selectedColor;
    }
});

//Add event listener for play button
const playButton = document.getElementById("playButton");
playButton.addEventListener("click", (e) => {
    window.location.href = "gameView1.html";
});
