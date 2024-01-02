# Nutrition Exhibition 
- Hunter - hwpruitt
- Shailesh - spaudel4 
- Sushil - ssharm37

## What Works
- Page navigation and redirection depending on if user is guess or member for some pages
- Gameplay and data connection for game 1, _Guess the Ingredients_
- Login/register/logout functionality
- Menu navigation and data generation for each Dining hall
- Stats page with calculated information about each user

## What is not done
- Game 2, _Calorie Count_, remains incomplete
- Abilty for stats to be regenerated on on subsequent visits
- Settings page
- Offline functionality
- Splash screen
- Ability to add recipes
- FAQ, Privacy Policy, Terms & Conditions

## Authentication/Authorization Processes
- User data is currently being stored in a users.json file, but we will integrate into SQL soon.
- The authentication process for signing in relies on comparing stored usernames and encrypted passwords, along with a salt value for each user. The encryption method relies on using the SHA512 cryptographic hash algorithm and functions from the Node.js [Crypto](https://www.google.com/url?q=https://nodejs.org/api/crypto.html&sa=D&source=docs&ust=1699646640829271&usg=AOvVaw3UAZdgOkjVwp9nW7NRwFvp) library.
- For access control, due to all users being allowed on most pages, the main access control came in the form of keeping logged-in users away from the login/register pages and serving the proper landing page. When users are not logged-in, the landing page offers an about section and encourages them to make an account, but for logged-in users, going to that page redirects to a page that will calculate and display the individual user’s stats. For implementing this, relevant pages include a script that checks if some user is logged in, and performs the redirection based on the result.

## Caching Strategy
- We did not make it to the offline functionality section of the project, but the general idea is that we would use the cache to store HTML and CSS files. Including JS would be ideal, but we would possibly need to make some structural changes to seperate the scripts that rely on our API from the scripts that provide simple page functionality (like navigation).

## Pages and Status
| Pages | Status | Wireframe |
| --- | --- | --- |
| Default Landing | 70% | [wireframe](https://github.ncsu.edu/engr-csc342/csc342-2023Fall-GroupS/blob/main/Proposal/Wireframes/Login%20and%20Landing%20Pages.png) (left)|
| Login | 90% | [wireframe](https://github.ncsu.edu/engr-csc342/csc342-2023Fall-GroupS/blob/main/Proposal/Wireframes/Login%20and%20Landing%20Pages.png) (middle)|
| Signup/Register | 40% | [wireframe](https://github.ncsu.edu/engr-csc342/csc342-2023Fall-GroupS/blob/main/Proposal/Wireframes/Login%20and%20Landing%20Pages.png) (middle)|
| User Landing/Stats | 70% | [wireframe](https://github.ncsu.edu/engr-csc342/csc342-2023Fall-GroupS/blob/main/Proposal/Wireframes/Login%20and%20Landing%20Pages.png) (right)|
| Browse Dining Halls | 50% | [wireframe ](https://github.ncsu.edu/engr-csc342/csc342-2023Fall-GroupS/blob/main/Proposal/Wireframes/Menu%20Pages.png) (left)|
| Individual Dining Hall | 50% | [wireframe](https://github.ncsu.edu/engr-csc342/csc342-2023Fall-GroupS/blob/main/Proposal/Wireframes/Menu%20Pages.png) (right)|
| Settings | 50% | [wireframe](https://github.ncsu.edu/engr-csc342/csc342-2023Fall-GroupS/blob/main/Proposal/Wireframes/Settings%20Views.png) |
| Gameview | 50% | [wireframe](https://github.ncsu.edu/engr-csc342/csc342-2023Fall-GroupS/blob/main/Proposal/Wireframes/Game%20Views.png) |

## API Endpoints
### Implemented
| Method | Route | Description |
| --- | --- | --- |
| GET | /recipes | Get all the recipes |
| GET | /recipes/hall/:hall | Gets all recipes associated with a specific hall |
| GET | /recipes/random | Retrieves a random recipe |
| GET | /recipes/:name | Get a specific recipe by name |
| GET | /ingredients | Retrieves all ingredients |
| GET | /ingredients/random | Retrieves a random ingredient |
| PUT | /users/g1w | Updates the current user's data to represent a new win at game 1 |
| PUT | /users/g1l | Updates the current user's data to represent a new guess at game 1 |
| POST | /users/login | Receives a username and password to log in |
| POST | /users/register | Receives a username and password to try to make a new account |
| POST | /users/logout | Removes the user from the session |
| POST | /users/current | Retrieves data on the current user |

### Planned
| Method | Route | Description |
| --- | --- | --- |
| PUT | /users/g2w | Updates the current user's data to represent a new win at game 2 |
| PUT | /users/g2l | Updates the current user's data to represent a new guess at game 2 |
| PUT | /recipes/:recipe | Update a recipe |
| POST | /recipe | Create a recipe |
| DELETE | /recipes | Delete all the recipes |
| DELETE | /user/:userID | Delete a specific user |

## ER Diagram (Entity Relationship)
![Screenshot 2023-11-10 at 3 06 08 PM](https://media.github.ncsu.edu/user/18801/files/b659f46b-c8fe-4f8a-92e0-3bd2640d5349)


## Individual Team Member Contributions
### Shailesh Paudel

### Hunter Pruitt

### Sushil Sharma

<hr>

## Citations
* User Avatars: https://robohash.org/
* Menu Button Icon: https://icon-library.com/icon/three-line-menu-icon-12.html
* Nutrition Exhibition Logo: Generated using Vista Print
* Oval Dining Hall Image: https://www.technicianonline.com/news/on-the-oval-serves-as-primary-food-location-for-centennial-campus/article_2ae3a134-2507-11e9-a952-7f390c8d511e.html
* Fountain Dining Hall Image: https://facilities.ofa.ncsu.edu/building/din/
* Clark Dining Hall Image: https://facilities.ofa.ncsu.edu/building/ch/
* A lot of code is based on various lecture material examples, but also W3Schools and MDN documentation. We have put in-code citations as well where necessary.

<hr>

## User Credentials (for testing) = [id] - [username] / [password]:
* 0 - bobrob / password
* 1 - jdoe / password
* 2 - student / ???
* 3 - hello / guy
* 4 - IronMan / SuperHero (case sensitive)
