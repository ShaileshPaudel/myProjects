import api from './APIClient.js';

const signupButton = document.querySelector('#signupButton');
const username = document.querySelector('#username');
const password = document.querySelector('#password');
const pfpCheckbox = document.getElementById("pfp");

const errorBox = document.querySelector('#errorbox');

//Redirect if already logged in
api.getCurrentUser().then(user => {
  //If got some user then this page shouldn't be visible, redirect
  document.location = '/stats.html';
}).catch(error => {
  //Otherwise do nothing
});

//TODO:
signupButton.addEventListener('click', e => {

    if (!username.value) {
      errorBox.classList.remove("hidden");
      errorBox.innerHTML = "Username is a required field.";
    } else if (!password.value) {
      errorBox.classList.remove("hidden");
      errorBox.innerHTML = "Password is a required field.";
    } else if (!pfpCheckbox.checked) {
      errorBox.classList.remove("hidden");
      errorBox.innerHTML = "You must check the box to continue.";
    } else {
      errorBox.classList.add("hidden");
      api.registerUser(username.value, password.value).then(userData => {
        document.location = "/stats.html";
      }).catch((err) => {
        errorBox.classList.remove("hidden");
        if(err.status === 401) {
          console.log('\nregister.js --> Username or password is invalid (error message).');
          errorBox.innerHTML = "Username already exists.";
        }
        else {
          console.log('\nregister.js --> Other (error message).');
          errorBox.innerHTML = err;
        }
      });
  

    }
});
