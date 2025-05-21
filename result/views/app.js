var app = angular.module('catsvsdogs', []);
var socket = io.connect();

var bg1 = document.getElementById('background-stats-1');
var bg2 = document.getElementById('background-stats-2');

app.controller('statsCtrl', function ($scope, $http) {
  $scope.aPercent = 50;
  $scope.bPercent = 50;

  function getSessionIdFromUrl() {
    var match = window.location.pathname.match(/\/results\/([^\/]+)/);
    return match ? match[1] : null;
  }

  // Fetch session details from API
  function fetchSessionDetails(sessionId) {
    $http.get('/api/session/' + sessionId)
      .then(function (response) {
        $scope.optionA = response.data.optiona;
        $scope.optionB = response.data.optionb;
        $scope.sessionTitle = response.data.title;
      })
      .catch(function (error) {
        console.error('Failed to fetch session details:', error);
      });
  }

  var updateScores = function () {
    socket.on('scores', function (json) {
      data = JSON.parse(json);
      var a = parseInt(data.a || 0);
      var b = parseInt(data.b || 0);

      var percentages = getPercentages(a, b);

      bg1.style.width = percentages.a + "%";
      bg2.style.width = percentages.b + "%";

      $scope.$apply(function () {
        $scope.aPercent = percentages.a;
        $scope.bPercent = percentages.b;
        $scope.total = a + b;
      });
    });
  };

  var init = function () {
    document.body.style.opacity = 1;
    updateScores();
    var sessionId = getSessionIdFromUrl();
    if (sessionId) {
      fetchSessionDetails(sessionId);
    } else {
      console.error('Session ID not found in URL');
    }
  };

  socket.on('message', function (data) {
    init();
  });
});

function getPercentages(a, b) {
  var result = {};

  if (a + b > 0) {
    result.a = Math.round(a / (a + b) * 100);
    result.b = 100 - result.a;
  } else {
    result.a = result.b = 50;
  }

  return result;
}