## Portfolio of Code

### Android Jetpack Compose Exercise: Upper and Lower

#### Code
[UpperAndLower](https://github.com/dgoldhirsch/portfolio/tree/master/UpperAndLower)

#### Demo
![upper-and-lower](https://github.com/user-attachments/assets/60445500-be9f-4601-8b82-e0a671d06471)

### Android Jetpack Compose Exercise: Earth And Moon

#### Code
The code is a self-contained Android Studio project in a subdirectory of this github repo:
[EarthAndMoon](https://github.com/dgoldhirsch/portfolio/tree/master/EarthAndMoon)

#### Acknowledgement
This code was adapted from a solution by Shreyas Muthkur.  I would not have known even where to start, if I hadn't seen his solution.
Another interesting approach is taken by Anmol Verma, as described in https://x.com/oianmol/status/1502690796885409796.

#### Demo

The app runs lots faster than the gif (below), the Moon orbiting the Earth about once per second.  The frame rate is 10 milliseconds.

* To top the animation, click anywhere on the screen;  the same, to restart it.
* If you drag your finger within the Moon's orbit, the legend, "Earth," will appear.

![earth-moon](https://github.com/user-attachments/assets/f0e7463c-b0d5-4e1d-b9e2-2118a43fb7e9)

#### Coding Considerations
There are at least two basically different approaches to animation in Jetpack Compose:

* Use a simple, single frame rate, computing the change of position of all objects per frame.  This is the approach taken here.
* Use separate "inifinite transitions" to animate the different, moving objects.

This example was simple enough--just the faster moving Moon and the slower moving Earth--to use a single, 10 millisecond frame rate.  With that requirement, it is easy to program a while-loop in a launched effect changing the position of the objects per frame.

If the application involved more complicated and seemingly independent movements among objects, it would be tiresome and limiting to have to compute the basic, position movements for the fastest, common frame rate.  Instead, we'd prefer to use Jetpack Compose's InfiniteTransition APIs that allow independent animations.  "Under the hood," one imagines, it is the Jetpack rendering library that deduces a reasonable, common framerate with which to animate the different transitions.

#### Astronomical Facts
The size of the Sun and the width of the Earth's orbit are both far too small.  The Sun is, actually, about 109 times the width of the Earth, and the width of the Earth's orbit around it is actually much, much, larger.

On the other hand, the depiction of the Moon is about right with respect to the Earth both in relative size (it's about 1/4 the width of the Earth) and its orbital distance from the Earth (about 12 times the width of the Moon).

The shape of the orbit of the Moon is misleading.  In real life, the Moon and Earth both orbit around a point within the Earth (but not its center).  The shape of the orbit of the Earth around the Sun is here shown as a circle.  In real life, it is _almost_ a circle--much closer to it than is the Moon's orbit around the Earth.

The relative speed of the Moon's orbit (around the Earth) to the Earth's (around the Sun) is true to life.  However, for the purpose of the user experience here, we choose two arbitrary factors:

* The moon completes one orbit in about 1 second.
* The frame rate is 10 milliseconds.
