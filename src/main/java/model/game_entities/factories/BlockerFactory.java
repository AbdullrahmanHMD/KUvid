package model.game_entities.factories;

import model.game_building.Configuration;
import model.game_entities.Blocker;
import model.game_entities.enums.EntityType;
import model.game_physics.hitbox.HitboxFactory;
import model.game_physics.path_patterns.PathPatternFactory;
import utils.Coordinates;

public class BlockerFactory {

    private static BlockerFactory blockerFactory = new BlockerFactory();

    private BlockerFactory() {
    }

    public static BlockerFactory getInstance() {
        if (blockerFactory == null) {
            blockerFactory = new BlockerFactory();
        }
        return blockerFactory;
    }

    public Blocker getBlocker(EntityType type) {// TODO : modify implementation.
        Coordinates defaultCoordinates = new Coordinates(0, 0);
        double blockingRadius = Configuration.getInstance().getUnitL() * 0.5;
        double explosionRadius = Configuration.getInstance().getUnitL() * 2;
        return new Blocker(defaultCoordinates, HitboxFactory.getInstance().getBlockerHitbox(),
                PathPatternFactory.getInstance().getBlockerPathPattern(), type, blockingRadius,
                explosionRadius);
    }
}
