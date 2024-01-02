import HTTPClient from "./HTTPClient.js";

const API_BASE = '/api';

export default {
  getCurrentUser: () => {
    return HTTPClient.get(API_BASE + '/users/current');
  },

  logIn: (username, password) => {
    let data = {
      username: username,
      password: password
    }
    return HTTPClient.post(API_BASE + '/users/login', data);
  },

  registerUser: (username, password) => {
    let data = {
      username: username,
      password: password
    }
    return HTTPClient.post(API_BASE + '/users/register', data);
  },

  logOut: () => {
    return HTTPClient.post(API_BASE + '/users/logout', {});
  },

  getRecipeByName: (name) => {
    return HTTPClient.get(API_BASE+`/recipes/${name}`);
  },

  getRecipesByHall: (hall) => {
    return HTTPClient.get(API_BASE+`/recipes/hall/${hall}`);
  },

  getRandomRecipe: () => {
    return HTTPClient.get(API_BASE+`/recipes/random`);
  },

  getRandomIngredient: () => {
    return HTTPClient.get(API_BASE+`/ingredients/random`);
  },

  getAllIngredients: () => {
    return HTTPClient.get(API_BASE+`/ingredients`); 
  },

  incrementG1Wins: (username) => {
    let data = {
      username: username,
    }
    return HTTPClient.put(API_BASE+`/user/g1w`, data);
  },

  incrementG1Guesses: (username) => {
    let data = {
      username: username,
    }
    return HTTPClient.put(API_BASE+`/user/g1l`, data);
  }
};