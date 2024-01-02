import api from './APIClient.js';

//Get data on which dininghall to display data for
let hall;
if (document.getElementById("Oval") != null) {
    hall = "Oval"
} else if (document.getElementById("Fountain") != null) {
    hall = "Fountain"
} else if (document.getElementById("Clark") != null) {
    hall = "Clark"
}

//Load the recipes
api.getRecipesByHall(hall).then(recipes => {
    generateTable(recipes);
});
  
// Function to generate the table rows
function generateTable(recipes) {
    const tableBody = document.getElementById("recipeTableBody");
    tableBody.innerHTML = '';

    recipes.forEach(recipe => {
        const row = document.createElement('tr');
        let ingredientsString = "";
        (recipe.ingredients).forEach(i => {
            ingredientsString += i.name + "<br>";
        });


        row.innerHTML = `<td>${recipe.name}</td>
        <td>${recipe.calories}</td>
        <td class = "ingredientCell">${ingredientsString}</td>`;
        tableBody.appendChild(row);
    });
}

