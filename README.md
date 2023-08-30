Spigot DrawLib
==============

Spigot DrawLib allows you to draw shapes in Minecraft as well as transform
and animate those shapes. Below you will find a few examples of what this gem
is capable of.

How it works
------------

1. Design a shape. It could be as simple as line, circle or square, but it could
   go as far as a shape composed of multiple simple shapes. You can keep composing
   shapes endlessly.
```Java
Circle c = new Circle(player.getLocation(), 15, 10);
```

2. Get a selection of blocks circumscribing the designed shape.
```Java
Selection s = Circle.select();
```

3. Stroke. You can paint something on the selected shape,
but also erase the drawing and revert the map to its original state.
```Java
s.stroke(Material.STONE);
```

4. Transform the selection, e.g. move up 1 block and redraw in new location.
```Java
s.restore();
Transformation t = new Mover(0, 1, 0); // moves up along Y axis
Selection s2 = s.transform(t.transformer());
s2.stroke(Material.STONE);
```

5. Or continuously animate the selection, e.g. in correspondence to sin squared function,
duration 5 seconds, looped.
```Java
Animation a = new Animation(s, t);
a.useEasing(Animation.SINE_SQUARED);
a.setDuration(5);
a.beforeStep(s -> s.restore());
a.afterStep(s-> s.stroke(Material.Stone));
a.loop();
```