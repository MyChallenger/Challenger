
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
    challenge.set("poster", user);
  }
  response.success();
});


// This function updates the backer to the current user
Parse.Cloud.define("backChallenge", function(request, response) {
  var challenge = new Parse.Object("Challenge");
  challenge.id = request.params.challengeId;
  var user = Parse.User.current();
  challenge.set("backer", user);
  challenge.save(null, { useMasterKey: true }).then(function() {
    // If I choose to do something else here, it won't be using
    // the master key and I'll be subject to ordinary security measures.
    response.success(true);
  }, function(error) {
    response.error(error);
  });
});
