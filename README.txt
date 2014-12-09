**** LameGame Readme ****

Running lamegame is simple, though it does require two instances (a 'server'
    and a 'client' instance)
1. Download the jar file
2. place it in its own directory
3. unzip the jar file in this directory
4. run the jar file in the background (on linux) as a server:
java -jar -Djava.library.path='<path-to-lwjgl-natives' lamegame.jar server &

note - the server does not have a headless option; do not close the black
window that opens.

5. run the jar file again as a client:
java -jar -Djava.library.path='<path-to-lwjgl-natives>' lamegame.jar <hostname> client

6. after the client window starts, select 'new game' and enter in 'localhost'
or the name of the computer that is running the server. then enter the port
number 4444 to start.

7. You can play the game using the w,a,s,d keys for camera movement, q,e for
rotational movement, and up,down,left,right for forward, back, left, and right
movement. b is the brake, right shift fires lasers. 

