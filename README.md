# ReflexGame

Implementation for reflex game.

Board is a CustomView that represents the behavior of the touchable squares.

Logic is hold on the GameCardViewModel and GameActivity reacts to its changes.



https://user-images.githubusercontent.com/26092448/178909782-b3fdb4e6-b4b4-4dad-a729-c307bce50be9.mp4

The user will have 3 seconds for every five turns, and after that the time is decreased by 90% of the current time.

If the user reaches to score 0 after starting the game, it will be restarted.
