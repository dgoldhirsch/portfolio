## Portfolio of Code

### Sample "Show Products" App

Basic app that scrolls through a list of product-like listings, using Jetpack Compose, Navigation, Hilt, and other 2024-current technologies.

[Products App](https://github.com/dgoldhirsch/portfolio/tree/master/Products)

![Screen Shot 2024-10-11 at 12 25 56 AM](https://github.com/user-attachments/assets/b3d74f59-e809-4783-9409-7ebc23c46985)

Demonstrates basic competence with standard tech components:

* MVVM Compose-with-Navigation Architecture
* Uni-directional, reactive architecture
* Hilt
* Room
* Mockk/Junit

### Simple Jetpack Compose UI Exercises

#### Bottom Sheet Exercise

Create a view with a top and bottom half (each a different color), and a button in each half.The top button brings up a modal that covers the full screen. The bottom button brings up a modal that covers the lower 50% of the screen. Inside this modal there’s a button that toggles the modal between 25% and 50% coverage of the screen.

[UpperAndLower](https://github.com/dgoldhirsch/portfolio/tree/master/UpperAndLower)

![upper-and-lower](https://github.com/user-attachments/assets/60445500-be9f-4601-8b82-e0a671d06471)

#### Earth and Moon Animation Exercise

Show an approximation of the Earth orbiting the Sun while the Moon orbits the Earth, using the actual, relative speeds of rotation (the Moon moves about 13 times faster than the Earth does).  If the user hovers (drags the pointer device) within the orbit of the Earth, show the name, "Earth" nearby.  Stop/start the animation whenever the user clicks the screen.

[EarthAndMoon](https://github.com/dgoldhirsch/portfolio/tree/master/EarthAndMoon)

The app runs lots faster than the gif (below), the Moon orbiting the Earth about once per second.  The frame rate is 10 milliseconds.

![earth-moon](https://github.com/user-attachments/assets/5c7d8a61-3ef3-4626-933f-3d61ab5403cc)

This code was adapted from a solution by Shreyas Muthkur.  I would not have known even where to start, if I hadn't first seen his solution.
Another interesting approach is taken by Anmol Verma, as described in https://x.com/oianmol/status/1502690796885409796, animating several planets plus their moons.

There are at least two basically different approaches to animation in Jetpack Compose:

* Use a simple, single frame rate, computing the change of position of all objects per frame.  This is the approach taken here.
* Use separate "inifinite transitions" to animate the different, moving objects.

This example was simple enough--just the faster moving Moon and the slower moving Earth--to use a single, 10 millisecond frame rate.  With that requirement, it is easy to program a while-loop in a launched effect changing the position of the objects per frame.

If the application involved more complicated and seemingly independent movements among objects, it would be tiresome and limiting to have to compute the basic, position movements for the fastest, common frame rate.  Instead, we'd prefer to use Jetpack Compose's InfiniteTransition APIs that allow independent animations.  "Under the hood," one imagines, it is the Jetpack rendering library that deduces a reasonable, common framerate with which to animate the different transitions.

##### Astronomical Facts
The size of the Sun and the width of the Earth's orbit are both far too small.  The Sun is, actually, about 109 times the width of the Earth, and the width of the Earth's orbit around it is actually much, much, larger.

On the other hand, the depiction of the Moon is about right with respect to the Earth both in relative size (it's about 1/4 the width of the Earth) and its orbital distance from the Earth (about 12 times the width of the Moon).

The shape of the orbit of the Moon is misleading.  In real life, the Moon and Earth both orbit around a point within the Earth (but not its center).  The shape of the orbit of the Earth around the Sun is here shown as a circle.  In real life, it is _almost_ a circle--much closer to it than is the Moon's orbit around the Earth.

The relative speed of the Moon's orbit (around the Earth) to the Earth's (around the Sun) is true to life.  However, for the purpose of the user experience here, we choose two arbitrary factors:

* The moon completes one orbit in about 1 second.
* The frame rate is 10 milliseconds.
