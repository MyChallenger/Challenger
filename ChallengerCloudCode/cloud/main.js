
// Use Parse.Cloud.define to define as many cloud functions as you want.
// For example:
Parse.Cloud.define("hello", function(request, response) {
  response.success("Hello world!");
});

Parse.Cloud.beforeSave(Parse.User, function(request, response) {
  var authData = request.object.get("authData");
  if(typeof authData !== 'undefined') {
    var facebook = request.object.get("authData").facebook;
    if(typeof facebook !== 'undefined') {
      request.object.set("provider", "facebook");
      request.object.set("providerId", request.object.get("authData").facebook.id);
      // console.log(request.object.get("authData"));
    }
  };
  response.success();
});
