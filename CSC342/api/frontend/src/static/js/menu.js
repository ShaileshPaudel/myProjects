import api from './APIClient.js';

const menu = document.getElementById("menu");
const menuButton = document.getElementById("menuButton");
const homeLink = document.getElementById("homeLink");
const brandDiv = document.getElementById("brandDiv");

/** 
 * Referenced Mozilla Node.contains documentation for detecting where user click is
 * Handles appearing / disappearing of menu depending on user click target.
 * If click is menuButton -> show menu
 * If click is outside menu but still on page -> unshow menu
 * Otherwise (if click is in menu) -> do nothing
*/
document.addEventListener("click", (e) => {
    if (menuButton.contains(e.target)) {
        menu.style.display = "flex";
    } else if (!menu.contains(e.target)) {
        menu.style.display = "";
    }
});

//Add redirection for clicking on the logo and title
brandDiv.addEventListener("click", (e) => {
    window.location.href = 'index.html';
});

//Populate user info for sidebar if it exists
const welcomeHeaderSidebar = document.getElementById("welcomeUser");
const userIcon = document.getElementById("userIcon");
api.getCurrentUser().then(user => {
    welcomeHeaderSidebar.innerHTML = "Welcome, " + user.username + "!";

    //Make home button redirect to stats page
    homeLink.href = "stats.html";

    //create an element for the pfp if logged in
    let pfp = document.createElement("img");
    pfp.src = user.avatar;
    userIcon.appendChild(pfp);

    //Get the logout button working
    let logoutLink = document.createElement('a');
    logoutLink.href = '#';
    logoutLink.innerHTML = "Logout";
    logoutLink.addEventListener("click", e => {
      e.preventDefault();
      api.logOut().then(() => {
        document.location = "/";
      });
    });

    document.getElementById("logoutSidebar").appendChild(logoutLink);


}).catch(error => {
    console.log("got an error, likely indicating no user is logged in:");
    console.log("error = " + error);

    //Make home button redirect to index page
    homeLink.href = "index.html";

    //if not logged in, hide the pfp container
    userIcon.style.display = "none";

    //Add a nav button to the login page
    let loginLink = document.createElement('a');
    loginLink.href = "login.html";
    loginLink.innerHTML = "Login";
    document.getElementById("logoutSidebar").appendChild(loginLink);

});

