// This code is from lecture materials for database2
// Importing the mysql module
const mysql = require('mysql');

// declare a variable that will be used to store the database connection
let connection;

// This check if the database connection exists and if not it creates a new connection
exports.getDatabaseConnection = () => {
  if(!connection) {
    connection = mysql.createPool({
      host: process.env.DB_HOST,
      port: process.env.DB_PORT,
      user: process.env.MYSQL_USER,
      password: process.env.MYSQL_PASSWORD,
      database: process.env.MYSQL_DATABASE,
      charset: process.env.DB_CHARSET
    })
  }
  return connection;
};

// Database query execution, it execute the sql query with the provided parameters
exports.query = (query, params = []) => {
  return new Promise((resolve, reject) => {
    if(!connection) {
      connection = exports.getDatabaseConnection();
    }
    connection.query(query, params, (err, results, fields) => {
      if(err) {
        reject(err);
        return;
      }
      resolve({
        results: results,
        fields: fields
      })
    })
  });
};

// closing the database connection, it checks if a conneciton exists and if so then calls connection.end() to close the connections. 
// then it sets the connection to null
exports.close = () => {
  if(connection) {
    connection.end();
    connection = null;
  }
};