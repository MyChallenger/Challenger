
// Use Parse.Cloud.define to define as many cloud functions as you want.
// For example:
Parse.Cloud.define("hello", function(request, response) {
  response.success("Hello world!");
});

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

Parse.Cloud.beforeSave("Challenge", function(request, response) {
  var challenge = request.object;
  if (challenge.isNew()) {
    var user = Parse.User.current();
    challenge.set("poster", user);
  }
  response.success();
});
