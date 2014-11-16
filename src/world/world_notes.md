# World Implementation

It looks like for our world because it is dynamic and in 3 dimensions collision
is a little bit harder.

For octrees it might be best to use a pregenerated tree of the world split up
and then use this to check for collisions in the smaller pieces of the world.

Still having some difficulties wrapping my head around octrees for 3d boxes
going to start with basic box collision.

Bounding boxes right now have no orientation and are expected to be in the same
coordinate system (or aligned in the same way)

It looks like dynamic scenes are difficult regarding collisions, might end up
using a precomputed octree which subdivides the world grid and checks for likely
collisions that way.
