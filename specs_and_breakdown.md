# LameGame
Created by Robert, Weston, Hans, Paige, and Aaron.

# This Document
The purpose of this incredibly important and interesting document is to act as
a centralized specification sheet for LameGame. A (hopefully final?) version of
the spec sheet will be turned into Joel Castellanos on 2014-11-07, as our grade
depends on it. 

This shit will aid Joel in evaluating our horrible performance on the project
at its completion, so he may easily assign F's to everyone. Joel, you could
just assign us an F now and save everyone the time.


# Brief Description of LameGame

LameGame is a first person shooter with spaceships. It's clear that no one has
ever attempted to build a game like this before. A player will have a view
of the spaceship from above and back, watching it move around as it goes
through space. The game environment will have various obstacles, notably
asteroids that can destroy a ship if not avoided or destroyed by the user. In
multiplayer mode, up to four archrivals can solve their problems by attacking
each other or tricking a user into hitting an asteroid. 

Littered throughout the game world, there will be various power-ups, weaponry,
and health refreshers that players may obtain by flying through them. Some
power ups may not be so powerful. Some may be very powerful indeed. 


# Scope
The scope of LameGame is mostly the five developers, our TA, Torin, and our
instructor, Joel - unless for some reason the general public finds a major
interest in our amazing game, LameGame. It is meant to be recreational for all
except for the developers. 


# Definitions
Teammates, please edit this  

* Server:
	A machine that serves as a centralized point of entry for users during
	multiplayer access. It can store user data and gameplay information,
	calculate physics and interactions, and handle placement of the
	aformentioned all-powerful powerups. The server's physical location has not
	been determined, though a machine in FEC may suffice for our purposes.
	Users can connect to the server through the Internet, because it's 2014.
	Packets and other information will be encrypted to keep dirty, rotten
	cheats away from our precious powerup data.

* UI :
	The user interface for LameGame. This serves as the primary (and only) way
	of interacting with the program. Options to reload levels, start games,
	connect to the server, pull user data from the server, close the game, and
	so on will be present through the UI. The gui will be created in Java's
	beautiful Swing lwt framework (or javafx).

* Player :
	Person who is playing the game to the best or worst of their abilities, in
	single-player or mulitplayer mode. Let no designation be made between
	'player' and 'user'. 

* Input:
	as a subcomponent of the UI, input will be handled through a
	Wimote/keyboard and mouse. Users will primarly use the keys ${W,A,S,D}$
	keys to navigate LameGame. Note that there is no significant output. 

* Graphics/ Grafiks:
	LameGame will be highly visual and beautiful. Some (members of the dev
	team) may say it looks like Rothko or another famous artist who
	didn't work in concrete shapes or forms. LameGame's graphics will be made
	using Java OpenGL (JOGL) via the Java Lightweight Game Library (jlgl). 
	Developers will attempt to avoid creating new acronyms. Graphics will
	include meshes, lighting, textures, immersion, moveable cameras, and
	"realistic" movement of the objects in space, including the player's ship
	and all other things. This will be a large chunk of work.

* Physics/Fizikz:
	LameGame will have a robust physics implemenation largely based on the
	Bullet engine. I have no idea how that engine works and probably should get
	cracking.

* World:
	The game's current world or map in which players may play. The world will
	be comprised of several decorative objects:
	* the world will have a starry background that is everpresent, like the stars
		themselves. 
	* The world will have far-away planets to provide a sense of place. These
		planets will hopefully move a little bit, because everything is moving in
		space.
	* the world will have 3d objects in it, primarily asteroids unless we have
		time to add more objects (e.g., space mines, space pirates, space
		ninjas, space princesses, space beach balls, space aliens,
		floating software specifications that devour you in one 'hit',
		evil Computer Science instructors that think you have nothing
		else to do but work on their class).

* Testing:
	A thing that young developers seldom do correctly but is crucial to the
	success of a larger project. Testing within LameGame will include two
	subcomponents:
	* Unit tests - these shall be written for each class or collection of
	classes that inherit each other. 
	* Integration test - these shall be performed to ensure that the disparate
	parts are hacked together in a way that works and doesn't break some other
	component in the process. It is the author's suspicion that this will be
	challenging.
	* 'Daily' builds - after the first semi-working version is complete, one
	member will be in charge of creating a 'daily build' of LameGame. This
	daily build will be a final bugfinding and integration testing measure .
	This daily build may not in fact be daily, but may happen many times per
	day as the deadline approaches. Also, the authors do not expect that
	compiling LameGame will take that long - it's not Windows Vista, after all.

* Version control / code reviews:
	Git. Github. Git. We students will be using Git/Github as a DVCS,
	implementing a standard feature-and-bugfix branch workflow because we all
	want to do things that future employers may like. A 'dev' branch and a
	'master' branch will be the primary lines of development, with merges into
	master being handeld by one group member who is crazy. We will make
	extensive use of personal branches and pull requests through github to
	ensure that code standards, code reviews, and tests are implemented
	correctly. 

* Game interaction:
	Any interaction between a Player and the world. This includes events such
	as picking up an incredible powerup or firing a space plasma rocket grenade
	laser at an archnemesis.

* Documentation:
	Refers to both JavaDoc, specifications documents like this one, notes, and
	smoke signals that relay information to each other or to an end
	user/player. Includes technical and non-technical docs.
	
# General breakdown of responsibilities
Now that you are bored to tears, we can assign labour.

Note that distribution of labor may change slightly as the project's
completion waxes and wanes, but for right now, our breakdown is as follows:

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

