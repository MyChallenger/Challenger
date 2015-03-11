
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


// This function updates the backer to the current user
Parse.Cloud.define("backChallenge", function(request, response) {
  var challenge = new Parse.Object("Challenge");
  challenge.id = request.params.challengeId;
  var user = Parse.User.current();
  challenge.set("status", "BACKED");
  challenge.set("backer", user);
  challenge.save(null, { useMasterKey: true }).then(function() {
    // If I choose to do something else here, it won't be using
    // the master key and I'll be subject to ordinary security measures.
    response.success(true);
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

  console.log("Updating leaderboard");

  // Set up to modify user data
  Parse.Cloud.useMasterKey();
  var counter = 0;
  // Query for all users
  var query = new Parse.Query(Parse.User);
  query.descending("pointsEarned");
  var previousPointsEarned = -1;
  query.find({
    success: function(results) {
      // Do something with the returned Parse.Object values
      for (var i = 0; i < results.length; i++) {
        var user = results[i];
        var currentPointsEarned = user.get("pointsEarned");
        console.log("User: " + user.get("name") + ", pointsEarned: " + currentPointsEarned);
        if (previousPointsEarned == currentPointsEarned) {
          console.log("Skipping pointsEarned " + currentPointsEarned);
          return;
        }
        counter += 1;
        var countQuery = new Parse.Query(Parse.User);
        // var GameScore = Parse.Object.extend("_User");
        // var countQuery = new Parse.Query(GameScore);
        console.log("Counting users with pointsEarned: " + currentPointsEarned);
        countQuery.equalTo("pointsEarned", currentPointsEarned);
        countQuery.count({
          success: function(count) {
            console.log("User: " + user.get("name") + ", pointsEarned: " + currentPointsEarned + ", Count: " + count);
            // Run the update query
            var samePointsQuery = new Parse.Query(Parse.User);
            samePointsQuery.equalTo("pointsEarned", currentPointsEarned);
            samePointsQuery.find({
              success: function(results) {
                // Do something with the returned Parse.Object values
                for (var i = 0; i < results.length; i++) {
                  var userToUpdate = results[i];
                  userToUpdate.set("leaderBoardRank", counter);
                  console.log("Updating User: " + user.get("name") + ", pointsEarned: " + currentPointsEarned + ", Rank: " + counter);
                  userToUpdate.save();
                }
              },
              error: function(error) {
                console.error("Could not update: " + error)
              }
            });
            counter += (count - 1);
          },
          error: function(error) {
            console.error("Could not count: " + error)
          }
        });
        // Update to plan value passed in
        // user.set("plan", request.params.plan);
        // var updateEveryXUsers = 100;
        var updateEveryXUsers = 1;
        if (counter % updateEveryXUsers === 0) {
          // Set the  job's progress status
          status.message(counter + " users processed.");
        }
        return user.save();
      }
    },
    error: function(error) {
      // Set the job's error status
      status.error("Uh oh, something went wrong: " + error);
    }
  }).then(function(results) {
    // Set the job's success status
    status.success("Leaderboard updated successfully.");
  });
  // query.each(function(user) {
  //   var currentPointsEarned = user.get("pointsEarned");
  //   if (previousPointsEarned == currentPointsEarned) {
  //     console.log("Skipping pointsEarned " + currentPointsEarned);
  //     return;
  //   }
  //   counter += 1;
  //   var countQuery = new Parse.Query(Parse.User);
  //   countQuery.equalTo("pointsEarned", currentPointsEarned);
  //   countQuery.count({
  //     success: function(count) {
  //       // Run the update query
  //       var samePointsQuery = new Parse.Query(Parse.User);
  //       samePointsQuery.equalTo("pointsEarned", currentPointsEarned);
  //       samePointsQuery.find({
  //         success: function(results) {
  //           // Do something with the returned Parse.Object values
  //           for (var i = 0; i < results.length; i++) {
  //             var userToUpdate = results[i];
  //             userToUpdate.set("leaderBoardRank", counter);
  //             userToUpdate.save();
  //           }
  //         },
  //         error: function(error) {
  //           console.error("Could not update: " + error)
  //         }
  //       });
  //       counter += (count - 1);
  //     },
  //     error: function(error) {
  //       console.error("Could not count: " + error)
  //     }
  //   });
  //   // Update to plan value passed in
  //   // user.set("plan", request.params.plan);
  //   // var updateEveryXUsers = 100;
  //   var updateEveryXUsers = 1;
  //   if (counter % updateEveryXUsers === 0) {
  //     // Set the  job's progress status
  //     status.message(counter + " users processed.");
  //   }
  //   return user.save();
  // }).then(function() {
  //   // Set the job's success status
  //   status.success("Leaderboard updated successfully.");
  // }, function(error) {
  //   // Set the job's error status
  //   status.error("Uh oh, something went wrong: " + error);
  // });
});
