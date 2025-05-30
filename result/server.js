const { time } = require('console');

var express = require('express'),
  async = require('async'),
  { Client } = require('pg'),
  cookieParser = require('cookie-parser'),
  app = express(),
  path = require('path'),
  server = require('http').Server(app),
  io = require('socket.io')(server);

var port = process.env.PORT || 4000;

io.on('connection', function (socket) {

  socket.emit('message', { text: 'Welcome!' });

  socket.on('subscribe', function (data) {
    socket.join(data.channel);
  });
});

var connectionString = process.env.VOTE_DB_URL || 'postgres://postgres:postgres@localhost:5432/cs544';

let dbClient = null;
const voteTimeouts = {}; // Add this at the top-level

async.retry(
  { times: 1000, interval: 1000 },
  function (callback) {
    new Client({ connectionString: connectionString, }).connect(function (err, client, done) {
      if (err) {
        console.error("Waiting for db", err);
      }
      callback(err, client);
    });
  },
  function (err, client) {
    if (err) {
      return console.error("Giving up");
    }
    console.log("Connected to db");
    dbClient = client; // Save client for later use
  }
);

function stopVotes(sessionId) {
  Object.values(voteTimeouts).forEach(timeout => {

    clearTimeout(timeout);
    delete voteTimeouts[timeout]
    console.log(`Stopped polling for session: ${timeout}`);
  });
}


function getVotes(client, sessionId) {
  client.query('SELECT option, COUNT(voter_id) AS count FROM vote where session_id = $1 GROUP BY option', [sessionId], function (err, result) {
    if (err) {
      console.error("Error performing query: " + err);
    } else {
      var votes = collectVotesFromResult(result);
      io.sockets.emit("scores", JSON.stringify(votes));
    }

    voteTimeouts[sessionId] = setTimeout(function () { getVotes(client, sessionId) }, 500);
  });
}




function collectVotesFromResult(result) {
  var votes = { a: 0, b: 0 };

  result.rows.forEach(function (row) {
    votes[row.option] = parseInt(row.count);
  });

  return votes;
}

app.use(cookieParser());
app.use(express.urlencoded());
app.use(express.static(__dirname + '/views'));

app.get('/', function (req, res) {
  res.sendFile(path.resolve(__dirname + '/views/index.html'));
});

app.get('/results/:sessionId', function (req, res) {

  const sessionId = req.params.sessionId;
  res.sendFile(path.resolve(__dirname + '/views/index.html'));

  // Wait for dbClient to be available before proceeding
  function waitForDbClient(retries = 20) {
    if (dbClient) {
      getVotes(dbClient, sessionId);
    } else if (retries > 0) {
      setTimeout(() => waitForDbClient(retries - 1), 500);
    } else {
      console.error("dbClient not available after waiting.");
    }
  }
  stopVotes(sessionId);
  waitForDbClient();
});

app.get('/api/session/:sessionId', async (req, res) => {
  console.log("Fetching session data for:", req.params.sessionId);
  const sessionId = req.params.sessionId;
  if (!dbClient) {
    return res.status(503).json({ error: 'Database not connected' });
  }
  try {
    const result = await dbClient.query(
      'SELECT optiona, optionb, title FROM session WHERE id = $1',
      [sessionId]
    );
    if (result.rows.length === 0) {
      return res.status(404).json({ error: 'Session not found' });
    }
    const { optiona, optionb, title } = result.rows[0];
    res.json({ optiona, optionb, title });
  } catch (err) {
    console.error('Error fetching session:', err);
    res.status(500).json({ error: 'Internal server error' });
  }
});



server.listen(port, function () {
  var port = server.address().port;
  console.log('App running on port ' + port);
});
