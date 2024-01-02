const express = require('express');
const apiRouter = require('./APIRoutes');

const app = express();
const PORT = process.env.PORT || 80;

app.use(express.json());
app.use(express.urlencoded({ extended: true }));

// Use the apiRouter for all routes starting with "/api"
app.use('/', apiRouter);

// Ask our server to listen for incoming connections
app.listen(PORT, () => console.log(`Server listening on port: ${PORT}`));