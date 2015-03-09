
// Use Parse.Cloud.define to define as many cloud functions as you want.
// For example:
Parse.Cloud.define("hello", function(request, response) {
  response.success("Hello world!");
});

Parse.Cloud.beforeSave(Parse.User, function(request, response) {
  var authData = request.object.get("authData");
  if(typeof authData !== 'undefined') {
    var facebook = request.object.get("authData").facebook;
    var twitter = request.object.get("authData").twitter;
    // console.log(request.object.get("authData"));
    // Determine the provider
    if(typeof facebook !== 'undefined') {
      request.object.set("provider", "FACEBOOK");
      request.object.set("providerId", request.object.get("authData").facebook.id);
    } else if(typeof twitter !== 'undefined') {
      request.object.set("provider", "TWITTER");
      request.object.set("providerId", request.object.get("authData").twitter.id);
    } else {
      request.object.set("provider", "PARSE");
    }
  };
  response.success();
});
