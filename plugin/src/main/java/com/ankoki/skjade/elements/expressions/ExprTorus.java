package com.ankoki.skjade.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.ankoki.skjade.utils.Shapes;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Torus/Giant Donut")
@Description("Returns the locations to make up a giant donut/torus.")
@Examples("play green spark at a giant donut around player's location with major radius 5 and minor radius 2")
@Since("insert version")
public class ExprTorus extends SimpleExpression<Location> {

    /*
!play green spark at a giant donut around player's location with major radius 5 and minor radius 2
     */
    static {
        Skript.registerExpression(ExprTorus.class, Location.class, ExpressionType.SIMPLE,
                "[a] (torus|[giant ]donut) around %location% with [a] major radius [of] %number% and [a] minor radius [of] %number% [with [a] density [of] %-number%]");
    }

    private Expression<Location> center;
    private Expression<Number> majorRadius;
    private Expression<Number> minorRadius;
    private Expression<Number> density;

    @Nullable
    @Override
    protected Location[] get(Event e) {
        if (center == null || majorRadius == null || minorRadius == null) return null;
        Location loc = center.getSingle(e);
        double major = majorRadius.getSingle(e).doubleValue();
        double minor = minorRadius.getSingle(e).doubleValue();
        double d = density == null ? 1 : density.getSingle(e).doubleValue();
        if (loc == null) return null;
        return Shapes.getTorus(loc, major, minor, d).toArray(new Location[0]);
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class<? extends Location> getReturnType() {
        return Location.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "torus around " + center.toString(e, debug) + " with a major radius of " + majorRadius.toString(e, debug) +
                " and a minor radius of " + minorRadius.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        center = (Expression<Location>) exprs[0];
        majorRadius = (Expression<Number>) exprs[1];
        minorRadius = (Expression<Number>) exprs[2];
        density = exprs.length == 4 ? (Expression<Number>) exprs[3] : null;
        return true;
    }
}
