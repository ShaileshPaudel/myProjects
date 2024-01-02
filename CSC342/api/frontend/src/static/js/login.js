import api from './APIClient.js';

const loginButton = document.querySelector('#loginButton');
const username = document.querySelector('#username');
const password = document.querySelector('#password');

const errorBox = document.querySelector('#errorbox');

//Redirect if already logged in
api.getCurrentUser().then(user => {
  //If got some user then this page shouldn't be visible, redirect
  document.location = '/stats.html';
}).catch(error => {
  //Otherwise do nothing
});


loginButton.addEventListener('click', e => {

  errorBox.classList.add("hidden");

  api.logIn(username.value, password.value).then(userData => {
    document.location = "/stats.html";
  }).catch((err) => {
    errorBox.classList.remove("hidden");
    if(err.status === 401) {
      console.log('\nlogin.js --> Username or password is invalid (error message).');
      errorBox.innerHTML = "Invalid username or password";
    }
    else {
      console.log('\nlogin.js --> User not found (error message).');
      errorBox.innerHTML = err;
    }
  });
});
