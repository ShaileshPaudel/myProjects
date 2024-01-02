let recipes = require('./data/foodItems.json');

//This file mimics making asynchronous request to a database
module.exports = {
    getRecipeByName: (name) => {
        return new Promise((resolve, reject) => {
          const recipe = recipes.find(recipe => recipe.name == name);
          console.log('\nRecipesDAO.js --> name = ' + recipe.name);
          if (recipe) { // recipe found
            console.log('\nRecipesDAO.js --> Recipe found!');
            resolve(recipe);
          } else {
            reject({code: 401, message: "No such recipe"});
          }
        });
      },

      getRandomRecipe: () => {
        return new Promise((resolve, reject) => {
          let randIdx = Math.floor(Math.random() * recipes.length);
          const recipe = recipes[randIdx];
          console.log('\nRecipesDAO.js --> name = ' + recipe.name);
          if (recipe) { // recipe found
            console.log('\nRecipesDAO.js --> Recipe found!');
            resolve(recipe);
          } else {
            reject({code: 401, message: "Cannot find random recipe, ensure recipe json is not empty"});
          }
        });
      },

      getRandomIngredient: () => {
        return new Promise((resolve, reject) => {
          let randRecipeIdx = Math.floor(Math.random() * recipes.length);
          const recipe = recipes[randRecipeIdx];
          let randIngredientIdx = Math.floor(Math.random() * recipe.ingredients.length);
          const ingredient = recipe.ingredients[randIngredientIdx];

          if (ingredient) { // pseudorandom ingredient selected
            console.log('\nRecipesDAO.js --> Ingredient found!');
            resolve(ingredient);
          } else {
            reject({code: 401, message: "Cannot find random ingredient, ensure recipe json is not empty and all recipes contain some ingredient"});
          }
        });
      },

      getRecipesByHall: (name) => {
        return new Promise((resolve, reject) => {
            const results = recipes.filter(recipe => recipe.diningHall == name);
            resolve(results);
          });
         
      },

      getAllIngredients: () => {
        return new Promise((resolve, reject) => {
          let list = [];
          recipes.forEach(recipe => {
            (recipe.ingredients).forEach(ingredient => {
              list.push(ingredient);
            });
          });
          resolve(list);
        });
      }
};
    