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

Note - Works correctly on Farris lab machines. Designed for and tested on linux
Ubuntu 12.04LTS.


# Gameplay:
## movement: 
  - w,a,s,d moves the camera, up,down,left,right move. 
  - f is a brake to stop movement
  - right shift fires lasers
  - p respawns if you die
  - o restarts the game if you are the winner and want to restart the game

# Notes:
  * Players are unable to fire correctly until the enter the gameboard. it is
    possible for players to start outside of the walls, but movement is forced
    to draw them in. This is to give a player the option to enter the world
    safely.
  * If a player is spawned with zero health (if someone kills you before you
    join the game), hit p to respawn.
