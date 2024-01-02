/* This function takes an event as parameter and sets the data that will be dragged 
during the drag and drop operation. */
function drag(event){
    event.dataTransfer.setData("text", event.target.id);
}

/* This funciton prevents the default behaviour of the drop event which is to 
not allow elements to be droppped into the target*/
function allowDrop(event){
    event.preventDefault();
}

/** This function handels the dropping the ingredients into a target area. It gets the data
 * that was set during the drag event and gets the id of the dragged ingredient to find the corresponding
 * ingredient in the DOM using getElementId. It also clones all child elements and text in the ingredient. It also
 * displays the delete button next to the ingredient while the ingredient is drop into the dropping zone and user can
 * delete the ingredient from the area by clicking the delete button. 
 */
function drop(event){
    event.preventDefault();
    let data = event.dataTransfer.getData("text");
    let ingredient = document.getElementById(data);

    let clone = ingredient.cloneNode(true);

    let deleteButton = document.createElement('button');
    deleteButton.textContent = "Delete";
    deleteButton.onclick = function(){
        deleteIngredient(clone);
    };
    clone.appendChild(deleteButton);

    event.target.appendChild(clone);
}

/** This function removes the ingredient element form the DOM*/
function deleteIngredient(ingredient){
    ingredient.remove();
}




