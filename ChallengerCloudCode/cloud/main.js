
// Use Parse.Cloud.define to define as many cloud functions as you want.
// For example:
Parse.Cloud.define("hello", function(request, response) {
  response.success("Hello world!");
});

// This function updates the provider and providerId when the user is created
// This is required because authData is not exposed to the Android SDK and there
// seems to be no good way to get the Facebook ID of any arbitrary user
// The Facebook ID is required to display the user's profile picture
Parse.Cloud.beforeSave(Parse.User, function(request, response) {
  var user = request.object;
  // Only do this during User creation.
  // This seems like a fair assumption at this point!
  if (user.isNew()) {
    var authData = user.get("authData");
    if(typeof authData !== 'undefined') {
      var facebook = user.get("authData").facebook;
      var twitter = user.get("authData").twitter;
      // console.log(user.get("authData"));
      // Determine the provider
      if(typeof facebook !== 'undefined') {
        user.set("provider", "FACEBOOK");
        user.set("providerId", user.get("authData").facebook.id);
      } else if(typeof twitter !== 'undefined') {
        user.set("provider", "TWITTER");
        user.set("providerId", user.get("authData").twitter.id);
      } else {
        user.set("provider", "PARSE");
      }
    };
  }
  response.success();
});

// This function updates the poster to the current user
// when the challenge is created
Parse.Cloud.beforeSave("Challenge", function(request, response) {
  var challenge = request.object;
  if (challenge.isNew()) {
    var user = Parse.User.current();
    challenge.set("status", "OPEN");
    challenge.set("poster", user);
  }
  response.success();
});

// This function updates the author to the current user
// when the comment is created
Parse.Cloud.beforeSave("Comment", function(request, response) {
  var comment = request.object;
  if (comment.isNew()) {
    var user = Parse.User.current();
    comment.set("author", user);
  }
  response.success();
});


// This function updates the backer to the current user
// AND
// This function updates the status to BACKED
Parse.Cloud.define("backChallenge", function(request, response) {
  var challenge = new Parse.Object("Challenge");
  challenge.id = request.params.challengeId;
  var user = Parse.User.current();
  challenge.set("status", "BACKED");
  challenge.set("backer", user);
  user.increment("challengesBacked");
  // TODO: How many points does the backer get?
  user.increment("pointsEarned", 50);
  challenge.save(null, { useMasterKey: true }).then(function() {
    // If I choose to do something else here, it won't be using
    // the master key and I'll be subject to ordinary security measures.
    var query = new Parse.Query(Parse.Object.extend("Challenge"));
    query.get(challenge.id, {
      success: function(fetchedChallenge) {
        // The object was retrieved successfully.
        var query = new Parse.Query(Parse.Installation);
        query.equalTo('user', fetchedChallenge.get("poster"));
        Parse.Push.send({
          where: query,
          data: {
            title: "Game on!",
            alert: "The challenge '" + fetchedChallenge.get("title") + "' has been backed by " + user.get("name"),
            customdata: {
              status: "BACKED",
              id: challenge.id
            }
          }
        }, {
          success: function() {
            console.log("Back Challenge push was successful");
            response.success(true);
          },
          error: function(error) {
            console.error(error);
            response.error(error);
          }
        });
      },
      error: function(object, error) {
        // The object was not retrieved successfully.
        // error is a Parse.Error with an error code and message.
        console.error(error);
        response.error(error);
      }
    });
  }, function(error) {
    response.error(error);
  });
});

// This function updates the status to COMPLETED
Parse.Cloud.define("completeChallenge", function(request, response) {
  var challenge = new Parse.Object("Challenge");
  challenge.id = request.params.challengeId;
  // TODO: Add a check if the current user is the poster
  var user = Parse.User.current();
  challenge.set("status", "COMPLETED");
  challenge.save(null, { useMasterKey: true }).then(function() {
    // If I choose to do something else here, it won't be using
    // the master key and I'll be subject to ordinary security measures.
    var query = new Parse.Query(Parse.Object.extend("Challenge"));
    query.get(challenge.id, {
      success: function(fetchedChallenge) {
        // The object was retrieved successfully.
        var query = new Parse.Query(Parse.Installation);
        query.equalTo('user', fetchedChallenge.get("backer"));
        Parse.Push.send({
          where: query,
          data: {
            title: "Verify this challenge!",
            alert: "The challenge '" + fetchedChallenge.get("title") + "' has been completed by " + user.get("name"),
            customdata: {
              status: "COMPLETED",
              id: challenge.id
            }
          }
        }, {
          success: function() {
            console.log("Complete Challenge push was successful");
            response.success(true);
          },
          error: function(error) {
            console.error(error);
            response.error(error);
          }
        });
      },
      error: function(object, error) {
        // The object was not retrieved successfully.
        // error is a Parse.Error with an error code and message.
        console.error(error);
        response.error(error);
      }
    });
  }, function(error) {
    response.error(error);
  });
});

// This function updates the status to VERIFIED
Parse.Cloud.define("verifyChallenge", function(request, response) {
  var challenge = new Parse.Object("Challenge");
  challenge.id = request.params.challengeId;
  // TODO: Add a check if the current user is the backer
  var user = Parse.User.current();
  challenge.set("status", "VERIFIED");
  challenge.save(null, { useMasterKey: true }).then(function() {
    // If I choose to do something else here, it won't be using
    // the master key and I'll be subject to ordinary security measures.
    var query = new Parse.Query(Parse.Object.extend("Challenge"));
    query.get(challenge.id, {
      success: function(fetchedChallenge) {
        // The object was retrieved successfully.
        var poster = fetchedChallenge.get("poster");
        poster.increment("pointsEarned", 100);
        poster.increment("challengesCompleted");
        poster.save(null, { useMasterKey: true }).then(function(obj) {
          var query = new Parse.Query(Parse.Installation);
          query.equalTo('user', poster);
          // the object was saved successfully.
          Parse.Push.send({
            where: query,
            data: {
              title: "Congratulations!",
              alert: "The challenge '" + fetchedChallenge.get("title") + "' has been verified by " + user.get("name") + ". You have earned 100 points.",
              customdata: {
                status: "VERIFIED",
                id: challenge.id
              }
            }
          }, {
            success: function() {
              console.log("Verify Challenge push was successful");
              response.success(true);
            },
            error: function(error) {
              console.error(error);
              response.error(error);
            }
          });
        }, function(error) {
          // the save failed.
          console.error(error);
          response.error(error);
        });
      },
      error: function(object, error) {
        // The object was not retrieved successfully.
        // error is a Parse.Error with an error code and message.
        console.error(error);
        response.error(error);
      }
    });
  }, function(error) {
    response.error(error);
  });
});

// Background jobs

// Update leaderboard
// curl -X POST \
// -H "X-Parse-Application-Id: gOqloKyikrHShtt0qNC9NcOpJipx2ijnVepC1dX1" \
// -H "X-Parse-Master-Key: f8SZh3nTWLdZRsKSE5oAByNygMhD73VdamA6BB7d" \
// -H "Content-Type: application/json" \
// -d '{}' \
// https://api.parse.com/1/jobs/updateLeaderboard
Parse.Cloud.job("updateLeaderboard", function(request, status) {

  var _ = require('underscore.js')
  console.log("Updating leaderboard");

  // Set up to modify user data
  Parse.Cloud.useMasterKey();
  var counter = 0;
  // Query for all users

  var query = new Parse.Query(Parse.User);
  query.descending("pointsEarned");

  query.find().then(function(results) {
    // Create a trivial resolved promise as a base case.
    var promise = Parse.Promise.as();
    var counter = 1;
    var previousPointsEarned = -1;
    _.each(results, function(result) {
      var user = result;
      var currentPointsEarned = user.get("pointsEarned");
      if (previousPointsEarned == currentPointsEarned) {
        console.log("Skipping user " + user.get("name") + ", pointsEarned " + currentPointsEarned);
        return;
      }
      previousPointsEarned = currentPointsEarned;
      console.log("Processing user " + user.get("name") + ", pointsEarned " + currentPointsEarned);
      // For each item, extend the promise with a function to delete it.
      promise = promise.then(function() {
        // Return a promise that will be resolved when the query is finished.
        // console.log("user = " + JSON.stringify(user));
        var samePointsQuery = new Parse.Query(Parse.User);
        // console.log("currentPointsEarned = " + currentPointsEarned);
        samePointsQuery.equalTo("pointsEarned", currentPointsEarned);
        return samePointsQuery.find();
      }).then(function(samePointsQueryResults) {
        // Collect one promise for each delete into an array.
        var promises = [];
        _.each(samePointsQueryResults, function(samePointsQueryResult) {
          var userWithSamePoints = samePointsQueryResult;
          // console.log(JSON.stringify(userWithSamePoints));
          userWithSamePoints.set("leaderBoardRank", counter);
          promises.push(userWithSamePoints.save());
        });
        counter += samePointsQueryResults.length;
        return Parse.Promise.when(promises);
      });
    });
    return promise;

  }).then(function() {
    // Every user was updated.
    var message = "Leaderboard updated successfully.";
    console.log(message);
    status.success(message);
  });
});
