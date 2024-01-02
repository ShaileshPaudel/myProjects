const path = require('path');
const usersFileLocation = path.join(__dirname, 'data', 'users.json');
let users = require(usersFileLocation);
const fs = require('fs'); //For updating the users JSON

//This file mimics making asynchronous request to a database
const crypto = require('crypto');

module.exports = {
  getUserByCredentials: (username, password) => {
    return new Promise((resolve, reject) => {
      const user = users.find(user => user.username == username);
      if (user) { // we found our user
        console.log('\nUserDAO.js --> User has been found!');
        crypto.pbkdf2(password, user.salt, 100000, 64, 'sha512', (err, derivedKey) => {
          if (err) { //problem computing digest, like hash function not available
            reject({ code: 400, message: "Error: " + err });
          }

          const digest = derivedKey.toString('hex');
          if (user.password == digest) {
            resolve(getFilteredUser(user));
          } else {
            reject({ code: 401, message: "Invalid username or password" });
          }
        });
      } else { // if no user with provided username
        reject({ code: 401, message: "No such user" });
      }
    });
  },

  createAccount: (username, password) => {
    return new Promise((resolve, reject) => {
      const user = users.find(user => user.username == username);
      //Check if username taken
      if (user) {
        console.log('\nUserDAO.js --> Username already taken');
        reject({ code: 401, message: "Username already taken" });
        return;
      }

      // Create and add the registered user object to the users.json file
      let nextId = users.length;
      generateUserObject(nextId, username, password)
        .then((newUser) => {
          resolve(getFilteredUser(newUser));
        })
    });
  },

  incrementUserG1Wins: (username) => {
    return new Promise((resolve, reject) => {
      const user = users.find(user => user.username == username);
      console.log("USERDAO incG1Wins")
      //Check if user exists
      if (!user) {
        reject({ code: 401, message: "No such user" });
      } else {
        //Increment the g1 wins
        user.game1Wins++;
        user.gamesPlayed++;
        user.game1Guesses++;

        //Update the Users JSON
        fs.writeFile(usersFileLocation, JSON.stringify(users, null, 2), (err) => {
          if (err) {
            console.error('Error updating users JSON file:', err);
            reject(err);
            return;
          }
          resolve(user);
        });
      }
    });
  },

  incrementUserG1Guesses: (username) => {
    return new Promise((resolve, reject) => {
      const user = users.find(user => user.username == username);
      console.log("USERDAO incG1Wins")
      //Check if user exists
      if (!user) {
        reject({ code: 401, message: "No such user" });
      } else {
        //Increment the g1 guesses
        user.game1Guesses++;

        //Update the Users JSON
        fs.writeFile(usersFileLocation, JSON.stringify(users, null, 2), (err) => {
          if (err) {
            console.error('Error updating users JSON file:', err);
            reject(err);
            return;
          }
          resolve(user);
        });
      }
    });
  },

  // getUserObject: (username) => {
  //   return new Promise((resolve, reject) => {
  //     const user = users.find(user => user.username == username);
  //     //Check if user exists
  //     if (!user) {
  //       reject({ code: 401, message: "No such user" });
  //     } else {
  //       // Return the user object
  //       resolve(user);
  //     }
  //   });
  // }

};

function getFilteredUser(user) {
  return {
    "id": user.id,
    "username": user.username,
    "gamesPlayed": user.gamesPlayed,
    "game1Guesses": user.game1Guesses,
    "game1Wins": user.game1Wins,
    "game2Guesses": user.game2Guesses,
    "game2Wins": user.game2Wins,
    "avatar": user.avatar
  };
}

// Generates a newly-created user object including the salt and hashed password
// Also updates the users.json file
function generateUserObject(idNum, username, password) {
  return new Promise((resolve, reject) => {
    const salt = crypto.randomBytes(30).toString('hex');
    const iterations = 100000;
    const keylen = 64; // Uses SHA-512 for 64 bytes

    crypto.pbkdf2(password, salt, iterations, keylen, 'sha512', (err, derivedKey) => {
      if (err) {
        console.error('Error when generating hashed password.', err);
        reject(err);
        return;
      }

      const avatarName = username;
      const avatarURL = `https://robohash.org/${avatarName}?size=64x64&set=set1`;
      const hashedPassword = derivedKey.toString('hex');

      const newUser = {
        id: idNum,
        username,
        gamesPlayed: 0,
        game1Guesses: 0,
        game1Wins: 0,
        game2Guesses: 0,
        game2Wins: 0,
        avatar: avatarURL,
        salt,
        password: hashedPassword
      };

      // Write the new users JSON to the file, using a indent of size 2 spaces
      users.push(newUser);
      console.log("\nusers = " + users);
      fs.writeFile(usersFileLocation, JSON.stringify(users, null, 2), (err) => {
        if (err) {
          console.error('Error updating users JSON file:', err);
          reject(err);
          return;
        }

        resolve(newUser);
      });
    });
  });
}
