package me.tolek.vanillaplus.libs;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class DrawLib {

    public static Selection circle(Location center, int radius, int step, int startArc, int endArc, Consumer<Location> worker) {
        final Selection selection = new Selection();
        for (int alpha = startArc; alpha < endArc; alpha += step) {
            final double dx = radius * Math.sin(Math.toRadians(alpha));
            final double dz = radius * Math.cos(Math.toRadians(alpha));
            selection.add(new Location(center.getWorld(), center.getBlockX() + 0.5 + dx, center.getY(), center.getBlockZ() + 0.5 + dz));
        }
        selection.getLocations().forEach(worker::accept);

        return selection;
    }

    public static Selection square(Location center, double length, boolean fill, BiConsumer<Location, Boolean> worker) {
        final int x = center.getX();
        final int y = center.getY();
        final int z = center.getZ();
        final double h = length / 2;

        final Selection vertices = new Selection();
        vertices.add(new Location(center.getWorld(), x - h, y, z - h));
        vertices.add(new Location(center.getWorld(), x - h, y, z + h));
        vertices.add(new Location(center.getWorld(), x + h, y, z - h));
        vertices.add(new Location(center.getWorld(), x + h, y, z + h));

        final Selection edges = new Selection();
        for (double d = -h + 1; d < h; ++d) {
            edges.add(new Location(center.getWorld(), x + d, y, z - h));
            edges.add(new Location(center.getWorld(), x + d, y, z + h));
            edges.add(new Location (center.getWorld(), x + h, y, z + d));
            edges.add(new Location (center.getWorld(), x - h, y, z + d));
        }

        vertices.getLocations().forEach(l -> worker.accept(l, true));
        edges.getLocations().forEach(l -> worker.accept(l, false));

        return Selection.union(vertices, edges);
    }

    public static void rectangle(Location loc, double lX, double lZ, boolean fill, Consumer<Location> worker) {
        final int x = loc.getBlockX();
        final int y = loc.getBlockY();
        final int z = loc.getBlockZ();
        final World w = loc.getWorld();

        if (fill) {
            for (int dX = 0; dX < lX; dX++) {
                for (int dZ = 0; dZ < lZ; dZ++) {
                    worker.accept(new Location(w, x + dX, y, z + dZ));
                }
            }
        } else {
            for (int dZ = 0; dZ < lZ; dZ++) {
                worker.accept(new Location(w, x, y, z + dZ));
            }
            for (int dX = 0; dX < lZ; dX++) {
                worker.accept(new Location(w, x + dX, y, z));
            }
            for (int dZ = 0; dZ < lZ; dZ++) {
                worker.accept(new Location(w, x + lX, y, z + dZ));
            }
            for (int dX = 0; dX < lZ; dX++) {
                worker.accept(new Location(w, x + dX, y, z + lZ));
            }
        }
    }

    public static void blockRectangle(Location loc, double lX, double lZ, boolean fill, Consumer<Location> worker) {
        final int x = loc.getBlockX();
        final int y = loc.getBlockY();
        final int z = loc.getBlockZ();
        final World w = loc.getWorld();

        if (fill) {
            for (int dX = 0; dX < lX; dX++) {
                for (int dZ = 0; dZ < lZ; dZ++) {
                    worker.accept(new Location(w, x + dX, y, z + dZ));
                }
            }
        } else {
            for (int dZ = 0; dZ < lZ; dZ++) {
                worker.accept(new Location(w, x, y, z + dZ));
            }
            for (int dX = 0; dX < lZ; dX++) {
                worker.accept(new Location(w, x + dX, y, z));
            }
            for (int dZ = 0; dZ < lZ; dZ++) {
                worker.accept(new Location(w, x + lX, y, z + dZ));
            }
            for (int dX = 0; dX < lZ; dX++) {
                worker.accept(new Location(w, x + dX, y, z + lZ));
            }
        }
    }

    public static Location getLocationInCircle(Location l, int radius, float yaw, float pitch) {
        double X = l.getX();
        double Y = l.getY();
        double Z = l.getZ();
        World world = l.getWorld();
        double X1 = X - radius * Math.sin(Math.toRadians(yaw)) * Math.abs(Math.cos(Math.toRadians(pitch)));
        double Z1 = Z + radius * Math.cos(Math.toRadians(yaw)) * Math.abs(Math.cos(Math.toRadians(pitch)));
        //double Y1 = Y - 0.7 * radius * Math.sin(Math.toRadians(pitch));

        Location EL = new Location(world, X1, Y, Z1, yaw, pitch);

        //p.sendMessage(ChatColor.RED + "Yaw: " + yaw + ", Pitch: " + pitch + ", X0: " + X + ", X1: " + X1 + ", Y: " + Y + ", Y1: " + Y1 + ", Z0: " + Z + ", Z1: " + Z1 + ", Action: " + a + ", SIN: " + 7 * Math.sin(Math.toRadians(pitch)));
        return EL;
    }

    public static Location getLocationInSphere(Location l, int radius, float yaw, float pitch) {
        double X = l.getX();
        double Y = l.getY();
        double Z = l.getZ();
        World world = l.getWorld();
        double X1 = X - radius * Math.sin(Math.toRadians(yaw)) * Math.abs(Math.cos(Math.toRadians(pitch)));
        double Z1 = Z + radius * Math.cos(Math.toRadians(yaw)) * Math.abs(Math.cos(Math.toRadians(pitch)));
        double Y1 = Y - 0.7 * radius * Math.sin(Math.toRadians(pitch));

        Location EL = new Location(world, X1, Y1, Z1, yaw, pitch);

        //p.sendMessage(ChatColor.RED + "Yaw: " + yaw + ", Pitch: " + pitch + ", X0: " + X + ", X1: " + X1 + ", Y: " + Y + ", Y1: " + Y1 + ", Z0: " + Z + ", Z1: " + Z1 + ", Action: " + a + ", SIN: " + 7 * Math.sin(Math.toRadians(pitch)));
        return EL;
    }

    public static void line(Location point1, Location point2, double space, Consumer<Location> worker) {
        World world = point1.getWorld();
        //Validate.isTrue(point2.getWorld().equals(world), "Lines cannot be in different worlds!");
        double distance = point1.distance(point2);
        Vector p1 = point1.toVector();
        Vector p2 = point2.toVector();
        Vector vector = p2.clone().subtract(p1).normalize().multiply(space);
        double length = 0;

        for (; length < distance; p1.add(vector)) {
            worker.accept(new Location(world, p1.getX(), p1.getY(), p1.getZ()));
            length += space;
        }
    }

    public static void blockLine(Location point1, Location point2, Consumer<Location> worker) {
        World world = point1.getWorld();
        //Validate.isTrue(point2.getWorld().equals(world), "Lines cannot be in different worlds!");
        double distance = point1.distance(point2);
        Vector p1 = point1.toVector();
        Vector p2 = point2.toVector();
        Vector vector = p2.clone().subtract(p1).normalize().multiply(1);
        double length = 0;

        for (; length < distance; p1.add(vector)) {
            worker.accept(new Location(world, p1.getX(), p1.getY(), p1.getZ()));
            length += 1;
        }
    }
}
