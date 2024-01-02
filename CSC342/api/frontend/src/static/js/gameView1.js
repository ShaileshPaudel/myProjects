import api from './APIClient.js';

const recipeLabel = document.getElementById("recipeToGuess");
const ingredientContainer = document.getElementById("ingredientContainer");
const guessButton = document.getElementById("guessButton");
const alertBox = document.querySelector(".alert");
const alertText = document.getElementById("alertText");

// Activate GUESS button
guessButton.addEventListener("click", checkAnswer);

setupGame();

function setupGame() {

    //Get current recipe randomly
    let currentRecipe = api.getRandomRecipe().then(recipe => {
        recipeLabel.innerHTML = recipe.name;
        recipeLabel.innerHTML += " [" + recipe.calories + " calories]"
        populateIng(recipe);
    });
}

/**
 * This function is used to combine the correct and incorrect ingredients and create buttons for each of the 
 * ingredients. 
 */
async function populateIng(recipe) {
    ingredientContainer.innerHTML = "";
    const allIngredients = await getBothIngredients(recipe.ingredients);
    const shuffledIng = shuffleIngredient(allIngredients);
    shuffledIng.forEach(ingredient => {
        const button = document.createElement("button");
        button.classList.add("ingredientButton");

        if (ingredient.isCorrect) {
            button.classList.add("correct");
        } else {
            button.classList.add("incorrect");
        }

        button.textContent = ingredient.name;

        button.addEventListener("click", function () {
            // Add a"highlighted" class on click
            this.classList.toggle("highlighted");
            if (this.classList.contains("highlighted")) {
                this.style.backgroundColor = "rgb(125, 245, 171)";
            } else {
                this.style.backgroundColor = "rgb(228, 231, 228)";
            }
        });

        ingredientContainer.appendChild(button);
    });
}

/**This funciton return the list of all ingredients, unique incorrect and correct*/
async function getBothIngredients(correctIngredients) {
    const currentIngredients = [];

    //Add the correct ingredients to current ingredients
    correctIngredients.forEach((correctIngredient) => {
        currentIngredients.push({
            name: correctIngredient.name,
            isCorrect: true,
        });
    });

    // Find a unique ingredient for each correct ingredient
    for (let j = 0; j < correctIngredients.length; j++) {
        let foundUniqueIngredient = false;
        while (!foundUniqueIngredient) {
            //Get random ingredient
            let randIngredient = await api.getRandomIngredient();
            // if correctIngredients contains it, continue, 
            if (currentIngredients.some(obj => obj.name === randIngredient.name)) {
                //Do nothing
            } else {
                //Add the ingredient ot incorrectIng
                foundUniqueIngredient = true;
                currentIngredients.push({
                    name: randIngredient.name,
                    isCorrect: false,
                });
            }
        }
    }

    return currentIngredients;
}

/**This function shuffles the ingredient using the math.random() */
function shuffleIngredient(array) {
    for (let i = array.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [array[i], array[j]] = [array[j], array[i]];
    }
    return array;
}

function checkAnswer() {
    // Check if all correct answers are highlighted
    let correctAnswers = document.querySelectorAll('.correct');
    for (let j = 0; j < correctAnswers.length; j++) {
        let current = correctAnswers[j];
        if (!current.classList.contains("highlighted")) {
            console.log(current.innerHTML + " is not highlighted when it should be");
            handleIncorrect(false);
            return;
        }
    }

    // Check if any incorrect answers are highlighted
    let incorrectAnswers = document.querySelectorAll('.incorrect');
    for (let j = 0; j < incorrectAnswers.length; j++) {
        let current = incorrectAnswers[j];
        if (current.classList.contains("highlighted")) {
            console.log(current.innerHTML + " is highlighted when it should NOT be");
            handleIncorrect(true);
            return;
        }
    }

    //If reached here, everything is correct!
    handleCorrect();
}

async function handleIncorrect(extra) {
    if (extra) {
        alertText.innerHTML = "At least one ingredient is incorrect. Please try again.";
    } else {
        alertText.innerHTML = "You are missing an ingredient. Please try again.";
    }

    alertBox.style.backgroundColor = "rgb(252, 113, 113)";
    alertBox.style.opacity = 100;
    alertBox.style.display = "flex";
    // Hide the alertBox after a certain amount of time
    setTimeout(
        function() {
            alertBox.style.transition = "opacity " + 0.3 + "s";
            alertBox.style.opacity = 0;
            alertBox.addEventListener("transitionend", function() {
            console.log("transition has ended, set display: none;");
            alertBox.style.display = "none";
          });
        }, 10000
      );

    api.getCurrentUser().then(user => {
        api.incrementG1Guesses(user.username);
    }).catch(error => {
        //Otherwise do nothing
    });
}

async function handleCorrect() {
    alertText.innerHTML = "Correct :D\n\nHere's a new game!";
    alertBox.style.backgroundColor = "green";
    alertBox.style.opacity = 100;
    alertBox.style.display = "flex";
    // Hide the alertBox after a certain amount of time
    setTimeout(
        function() {
            alertBox.style.transition = "opacity " + 0.3 + "s";
            alertBox.style.opacity = 0;
            alertBox.addEventListener("transitionend", function() {
            console.log("transition has ended, set display: none;");
            alertBox.style.display = "none";
          });
        }, 10000
      );

    api.getCurrentUser().then(user => {
        api.incrementG1Wins(user.username);
    }).catch(error => {
        //Otherwise do nothing
    });

    setupGame();

}

/** 
 * Returns a random Integer value between min & max (both inclusive) 
 * This code is based on the MDN documentation:
 * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/random 
 */
function getRandomNumber(min, max) {
    min = Math.ceil(min);
    max = Math.floor(max);
    return Math.floor(Math.random() * (max - min + 1) + min);
}

function getIngredientList() {
    const allIngredientsList = [];
    return api.getAllIngredients().then(ingredientList => {
        //console.log("\ingredientList[3].name = " + ingredientList[3].name);
        //console.log("\ingredientList.length = " + ingredientList.length);
        for (let i = 0; i < ingredientList.length; i++) {
            //console.log("ingredientList[i].name = " + ingredientList[i].name);
            allIngredientsList.push(ingredientList[i].name);
        }
        return allIngredientsList;
    });
}

