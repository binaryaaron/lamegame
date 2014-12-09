# LameGame Readme

Running lamegame is simple, though it does require two instances (a 'server'
    and a 'client' instance)

1. Download the jar file
2. place it in its own directory
3. unzip the jar file in this directory - note, the JAR file has all project
    resources and source code within it and needs to be unzipped. You can do this
    by running. Source code is in the 'src' folder. 

``` unzip lamegame.jar

4. run the jar file in the background (on linux) as a server:
java -jar -Djava.library.path='<path-to-lwjgl-natives' lamegame.jar server 

note - the server does not have a headless option; do not close the black
window that opens.

5. run the jar file again as a client (no args):

``` java -jar -Djava.library.path='<path-to-lwjgl-natives>' lamegame.jar 

6. after the client window starts, select 'new game' and enter in 'localhost'
or the name of the computer that is running the server. then enter the port
number 4444 to start.



# Gameplay:
## movement: 
  - w,a,s,d moves the camera, up,down,left,right move. 
  - f is a brake to stop movement
  - right shift fires lasers
  - p respawns if you die
  - o restarts the game if you are the winner and want to restart the game

# Notes:
  Players are unable to fire correctly until the enter the gameboard. it is
  possible for players to start outside of the walls, but movement is forced
  to draw them in. This is to give a player the option to enter the world
  safely.

# lamegame
LameGame is a first person shooter with spaceships, made as the final project
for CS351 at UNM. It's clear that no one has ever attempted to build a game
like this before. A player will have a view of the spaceship from above and
back, watching it move around as it goes through space. The game environment
will have various obstacles, notably asteroids that can destroy a ship if not
avoided or destroyed by the user. In multiplayer mode, up to four archrivals
can solve their problems by attacking each other or tricking a user into
hitting an asteroid. 

Littered throughout the game world, there will be various power-ups, weaponry,
and health refreshers that players may obtain by flying through them. Some
power ups may not be so powerful. Some may be very powerful indeed. 


## Contributors:
* Robert Nicholson rnicholson@unm.edu
* [Weston Ortiz](https://github.com/wortiz) weston@wortiz.com
* Hans
* Paige
* [Aaron Gonzales](http://github.com/xysmas)

# workflow notes
Please make a new branch for each feature or fix you are implementing and when
it's finished, submit a pull request to merge it. This workflow can be changed
if needed or wanted. Please do not commit directly to master. Pull requests
will be rejected if the merge breaks a test or doesn't conform to the coding
standard set aside by Joel Almighty, including clear comments and javadoc. 


# General breakdown of responsibilities
Note that distribution of labor may change slightly as the project's
completion waxes and wanes, but for right now, our breakdown is as follows:

The order in which things are listed could be changed to be in order of
importance, perhaps.

## Robert (Dan)
  * Implementing graphics (not as simple as it may sound!)
  * Implementing Audio / OpenAL
  * Moral support

## Paige
 Graphics implementation to represent game state. This is to be run on the
 local machine of each player in lwgl. I will add and implement models,
 textures, and lighting for the game. I will also focus on UI/UX. 

## Weston Ortiz
  * General physics / game interaction
  * Helping with the server and communication
  * Collisions / network related logic for keeping collisions correct on client
	side, while server does most of the collision checking (hits/pickups)
  * '2nd lead' assisting with integration if needed

## Hans
  * Server and client communication
  * Containers for client game-state update
  * Game physics
  * "that guy"

## Aaron Gonzales
  * "Leader" or something like it.
  * tracking development process and assigning release dates
  * Testing 
  * Daily builds
  * VC/ Code reviews
  * Assist with server and physics implementations
  * OO design and layout for subcomponents
  * Performance testing
  * Documentation
  * pedantic musings
  * possibly build a neural network AI for an adversary in single player




# installtion





# Documentation
  It's in the [docs](https://github.com/xysmas/lamegame/tree/master/docs)
  folder.
  * [Specifications (remember, we're graded on these):](https://github.com/xysmas/lamegame/tree/master/docs)
