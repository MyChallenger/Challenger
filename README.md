Project “Challenge”

User Stories :

1. Users can register using the Facebook credentials. Integrate with Facebook API for Identity management
2. When user successfully logs in. He is taken to the HomeScreen.
3. HomeScreen can be fragmented into 2 timelines, One to view the challenges timeline another tab to view the stream of top challenges that were completed and videos or photos associated with it. Sorting of the videos are based on popularity and time(Most popular videos today)  
4. Challenges timeline has list of challenges that are still open and user can accept.
5. Clicking on the item in the list of challenges will launch a new activity to view a detailed view of the challenge. This has the Challenge information like
  * Challenger's information, profile name, points(from donations made earlier)
  * Details about the challenge itself. Ex: Dressing up like a cowboy and signing on Market st
  * Expiry time on the challenge
  * The organization that will benefit from this challenge
  * Price of donation
  * User button accept or ignore button
6. If User clicks on ignore this challenge will be removed from the users Hometimeline and user will be directed back to the list of challenges.
7. If the user clicks accept then a timer will indicate the time left to fulfill the challenge and a link to upload the video. The Challenger will be notified that the challenge was accepted. (Open: Not sure of the design here. Should we create a Fragment that gets added on to the Challenge detail view. One fragment for user, the other for challenger with different message)
8. Once the challenge is completed the user uploads video showing the task being completed and the challenger verifies the video and once he accepts this will be available for everyone to see. And donations will be made to the organization. And points will be rewarded to both Challenger and user who completed the challenge.
9. (Open) What happens if the challenger does not accept the completed video, Someone needs to verify it?
10. HomeScreen 2nd tab contains list of top challenges that were completed. It has a like button to vote up. Challenge Name, User who completed it and a thumbnail of the view. Once you click on the item, a detailed view will let you see more information like time completed and posted, donation made, organization and will let you play the video.
11. On the homescreen, User can click on the ActionBar button to view his profile. The profile contains his Profile photo, Username, last challenge accepted, Score, Rank
12. ActionBar also as a button to add a new Challenge. Clicking on this button would take the user to a compose Challenge activity. Where user will need to enter the following information
  * Challenge,
  * Organization to benefit
  * Credit card information
  * Expiry time
  * Donation amount
