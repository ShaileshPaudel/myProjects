import api from './APIClient.js';

//Redirect if guest user
api.getCurrentUser().then(user => {
    //No redirect if logged in user
}).catch(error => {
    //If got some user then this page shouldn't be visible, redirect
    document.location = '/index.html';
});


const defaultColor = "#ffffff";
const selectedColor = "#808080";

const buttonAbout1 = document.getElementById("showAbout1");
const buttonAbout2 = document.getElementById("showAbout2");

const aboutP1 = document.getElementById("aboutP1");
const aboutP2 = document.getElementById("aboutP2");

const gamesPlayed = document.getElementById("gamesPlayed");
const g1AccuracyElement = document.getElementById("g1Accuracy");
const g2AccuracyElement = document.getElementById("g2Accuracy");
const winRateElement = document.getElementById("winRate");

// Update the stats section upon loading the page each time
window.onload = function () {
    showStats();
}

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

function showStats() {
    api.getCurrentUser().then(user => {
        gamesPlayed.innerHTML = "Games Played - " + user.gamesPlayed;

        let g1Accuracy;
        if (user.game1Guesses == 0) {
            g1Accuracy = "N/A";
        } else {
            g1Accuracy = (user.game1Wins / user.game1Guesses).toFixed(2);
        }    
        g1AccuracyElement.innerHTML = "<i>Guess the Ingredients</i> Accuracy - " + g1Accuracy;
    
        g2AccuracyElement.innerHTML = "<i>Calorie Count</i> Accuracy - N/A"
    
        let winRate;
        if (user.gamesPlayed == 0) {
            winRate = "N/A";
        } else {
            winRate = ((user.game1Wins + user.game2Wins) / user.gamesPlayed).toFixed(2);
        }    
    
        winRateElement.innerHTML = "Overall Win Rate - " + winRate;
    
    }).catch(error => {
        //Otherwise do nothing
        console.log("\nUnable to retrieve stats from user.");
        console.log("Error is -> " + error);
        //console.log("\nStats Unavailable: User is playing as a Guest.");
    });
}
