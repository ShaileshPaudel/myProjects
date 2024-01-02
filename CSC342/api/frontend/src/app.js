const express = require('express');

const frontendRouter = require('./routes/FrontendRoutes');
// const apiRouter = require('./APIRoutes');

const app = express();
const PORT = process.env.PORT;

app.use(express.json());
app.use(express.static(__dirname + '/static'));

app.use(frontendRouter);
// app.use("/api", apiRouter);

// Ask our server to listen for incoming connections
app.listen(PORT, () => console.log(`Server listening on port: ${PORT}`));