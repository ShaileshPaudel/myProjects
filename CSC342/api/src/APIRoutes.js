const express = require('express');
const cookieParser = require('cookie-parser');

const apiRouter = express.Router();
apiRouter.use(express.json());
apiRouter.use(cookieParser());

const UserDAO = require('./UserDAO');
const RecipesDAO = require('./RecipesDAO');
const { TokenMiddleware, generateToken, removeToken } = require('./TokenMiddleware');

let ingredients = require('../src/data/ingredients.json');
let ingredientCalories = require('../src/data/ingredientCalories.json');

apiRouter.get('/recipes', (req, res) => {
  res.json(recipes);
});

//Get all recipes associated with a specific hall
apiRouter.get('/recipes/hall/:hall', (req,  res) => {
  const hall = req.params.hall;
  RecipesDAO.getRecipesByHall(hall).then(recipes => {
    res.json(recipes);
  }).catch(err => {
    res.status(500).json({error: 'Internal server error'});
  });
});

//Get a random recipe
apiRouter.get('/recipes/random', (req,  res) => {
  RecipesDAO.getRandomRecipe().then(recipe => {
    res.json(recipe);
  }).catch(err => {
    res.status(500).json({error: 'Internal server error'});
  });
});

//Get a random ingredient
apiRouter.get('/ingredients/random', (req,  res) => {
  RecipesDAO.getRandomIngredient().then(ingredient => {
    res.json(ingredient);
  }).catch(err => {
    res.status(500).json({error: 'Internal server error'});
  });
});

//Get a specific recipe
apiRouter.get('/recipes/:name', (req,  res) => {
  const name = req.params.name;
  RecipesDAO.getRecipeByName(name).then(recipe => {
    res.json(recipe);
  }).catch(err => {
    res.status(404).json({error: 'Recipe not found'});
  });
});

// Get a list of all ingredients
apiRouter.get('/ingredients', (req, res) => {
  RecipesDAO.getAllIngredients().then(list => {
    res.json(list);
  }).catch(err => {
    res.status(404).json({error: 'Could not get all ingredients.'});
  });
});


apiRouter.post('/users/login', (req, res) => {
  console.log('\nAPIRoutes.js --> Entered apiRouter.post(login) function.');
  if (req.body.username && req.body.password) {
    UserDAO.getUserByCredentials(req.body.username, req.body.password).then(user => {
      let result = {
        user: user
      }
      console.log('\nAPIRoutes.js --> Entered apiRouter.post() function.');
      console.log('\nAPIRoutes.js --> user (Object) = ' + JSON.stringify(user));

      generateToken(req, res, user);

      res.json(result);
    }).catch(err => {
      console.log(err);
      res.status(err.code).json({ error: err.message });
    });
  } else {
    res.status(401).json({ error: 'Not authenticated' });
  }
});

apiRouter.put('/user/g1w', (req, res) => {
  UserDAO.incrementUserG1Wins(req.body.username).then(result => {
    res.json(result);
  }).catch(err => {
    console.log(err);
    res.status(err.code).json({ error: err.message });
  });
});

apiRouter.put('/user/g1l', (req, res) => {
  UserDAO.incrementUserG1Guesses(req.body.username).then(result => {
    res.json(result);
  }).catch(err => {
    console.log(err);
    res.status(err.code).json({ error: err.message });
  });
});

apiRouter.post('/users/register', (req, res) => {
  console.log('\nAPIRoutes.js --> Entered apiRouter.post(register) function.');
  if (req.body.username && req.body.password) {
    UserDAO.createAccount(req.body.username, req.body.password).then(user => {
      let result = {
        user: user
      }

      generateToken(req, res, user);

      res.json(result); // old may still need to use somehow?
      
      // Doesnt seem to do anything atm :(
      //res.redirect('/stats.html');
    }).catch(err => {
      console.log(err);
      res.status(err.code).json({ error: err.message });
    });
  } else {
    res.status(401).json({ error: 'Not authenticated' });
  }
});

apiRouter.post('/users/logout', (req, res) => {
  removeToken(req, res);

  res.json({ success: true });
});


apiRouter.get('/users/current', TokenMiddleware, (req, res) => {
  res.json(req.user);
});


module.exports = apiRouter;

