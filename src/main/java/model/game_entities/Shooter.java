package model.game_entities;

import model.game_building.Configuration;
import model.game_building.GameConstants;
import model.game_entities.enums.EntityType;
import model.game_entities.enums.SuperType;
import model.game_physics.hitbox.HitboxFactory;
import model.game_physics.path_patterns.PathPatternFactory;
import model.game_running.CollisionVisitor;
import model.game_running.ProjectileContainer;
import utils.Coordinates;
import utils.MathUtils;
import utils.Vector;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Shooter extends Entity {
    private Projectile currentProjectile;
    private ProjectileContainer container;

    Configuration config = Configuration.getInstance();

    // TODO move to game configuration
    private final double DEFAULT_ROTATION_STEP = 10;
    public static Logger logger = Logger.getLogger(Shooter.class.getName());

    public Shooter(ProjectileContainer container) {
        // Turn off logger
        logger.setLevel(Level.OFF);

        // sets the initial coordinates
        // TODO: get initial coords from the game configuration
        setCoordinates(new Coordinates(
                config.getGameWidth() / 2.0,
                config.getGameHeight() - 0.5 * config.getUnitL() *
                        GameConstants.SHOOTER_HEIGHT));

        // sets the Hitbox
        setHitbox(HitboxFactory.getInstance().getShooterHitbox());
        this.superType = SuperType.SHOOTER;
        this.container = container;
        this.setCurrentProjectile(this.nextAtom());
    }

    /**
     * Shoot the projectile on the tip of the shooter to the game space
     *
     * @return the atom on the tip of the shooter
     */
    public Projectile shoot() {
        if (getCurrentProjectile() == null)//get atom from the container returned null. (no more of the selected type)
            return null;

        this.adjustProjectilePosition();
        return this.reload();
    }

    /**
     * Adjust the projectile coordinates and speed vector orientation according to the coordinates and orientation
     * of the shooter
     */
    private void adjustProjectilePosition() {
        getCurrentProjectile().getHitbox().rotate(getAngle());
        getCurrentProjectile().setPathPattern(PathPatternFactory.getInstance().getAtomPathPattern(getHitbox().getRotationDegree()));
        getCurrentProjectile().setCoordinates(getShootingCoords());
    }


    /**
     * @return the coordinate of the projectile where it will start moving
     */
    private Coordinates getShootingCoords() {
        int height = (int) getHitbox().getHeight();
        int projectileRadius = (int) getCurrentProjectile().getHitbox().getHeight() / 2;
        double theta = MathUtils.angleComplement(this.getHitbox().getRotationDegree());

        int newHeight = MathUtils.getCompositeYComponent(projectileRadius, height / 2, theta);
        int newWidth = MathUtils.getCompositeXComponent(projectileRadius, height / 2, theta);

        return MathUtils.translate(this.getCoordinates(), new Coordinates(newWidth, -newHeight));
    }


    /**
     * reload the atom shooter by placing a random atom on the tip of the shooter
     *
     * @return the current projectile at the atom
     */
    public Projectile reload() {
        Projectile tmp = getCurrentProjectile();
        this.setCurrentProjectile(this.nextAtom());
        tmp.setVelocity(tmp.getSpeedPercentage());
        return tmp;
    }

    /**
     * get the next random atom
     *
     * @return a random atom
     */
    public Atom nextAtom() {
        return container.getRandomAtom(this.getCoordinates());
    }

    public Projectile getCurrentProjectile() {
        return currentProjectile;
    }

    public void setCurrentProjectile(Projectile currentProjectile) {
        this.currentProjectile = currentProjectile;
    }

    public void setPowerup(EntityType type) {
        Projectile previousProjectile = getCurrentProjectile();
        Powerup currentPowerup = container.getPowerUp(this.getCoordinates(), type);
        if (currentPowerup != null) {
            if (previousProjectile.superType == SuperType.ATOM)
                container.increaseAtoms(previousProjectile.getType().getValue(), 1, previousProjectile);
            else
                container.addPowerUp((Powerup) previousProjectile);
            setCurrentProjectile(currentPowerup);
        }
    }

    public void switchAtom() {
        // @REQUIRES: the the projectileContainer not to be empty nor the currentProjectile on the tip of the shooter to be of type powerup.
        // @MODIFIES: projectileContainer
        // @EFFECTS: changes the atom on the tip of the shooter to an atom of different type.
        Projectile previousProjectile = getCurrentProjectile();
        Projectile nextAtom = nextAtom();

        if (nextAtom != null) {
            if (previousProjectile.getSuperType() == SuperType.ATOM) {
                container.increaseAtoms(previousProjectile.getType().getValue(), 1, previousProjectile);
                while (previousProjectile.getType() == nextAtom.getType() && !uniqueTypeAvilable()) {
                    container.increaseAtoms(nextAtom.getType().getValue(), 1, nextAtom);
                    nextAtom = nextAtom();
                }
                setCurrentProjectile(nextAtom);
            } else {
                container.addPowerUp((Powerup) previousProjectile);
                setCurrentProjectile(nextAtom);
            }
        }
    }

    private boolean uniqueTypeAvilable() {

        int[] types = container.getAtomMap();
        int counter = 0;
        for (int type : types)
            if (type == 0)
                counter++;
        return counter == (types.length - 1);
    }

    public double getAngle() {
        return getHitbox().getRotationDegree();
    }

    public boolean rotate(int direction) {
        if (!checkLegalMovement(this.getCoordinates(), this.getAngle() + DEFAULT_ROTATION_STEP * direction))
            return false;
        getHitbox().rotate(DEFAULT_ROTATION_STEP * direction);
        return true;
    }

    public boolean move(int direction) {
        Coordinates newCoords = new Coordinates(getCoordinates().getX() + direction * config.getShooterSpeed(), getCoordinates().getY());
        if (!checkLegalMovement(newCoords, this.getAngle())) {
            logger.info("[Shooter] shooter cannot move to the new coordinates" + this.getCoordinates());
            return false;
        }
        this.setCoordinates(newCoords);
        logger.info("[Shooter] shooter moved to a new coordinates" + this.getCoordinates());
        return true;
    }

    /**
     * Check if the shooter config.getShooterSpeed() is within the game view
     *
     * @param c
     * @param angle
     * @return
     */
    private boolean checkLegalMovement(Coordinates c, double angle) {
        double gunWidth = config.getUnitL() * GameConstants.SHOOTER_WIDTH;
        if (c.getX() + gunWidth / 2 > config.getGamePanelDimensions().getWidth())
            return false;
        else if (c.getX() - gunWidth / 2 < 0)
            return false;
        return checkLegalAngle(c, angle);
    }

    /**
     * Check if the shooter rotation is within the game view
     *
     * @param c
     * @param angle
     * @return
     */
    private boolean checkLegalAngle(Coordinates c, double angle) {
        double gunWidth = config.getUnitL() * GameConstants.SHOOTER_WIDTH;
        double gunHeight = config.getUnitL() * GameConstants.SHOOTER_HEIGHT;

        // assume the left side if the shooter is in the left half of the screen, and right otherwise
        Vector rotatedShooter;
        if (c.getX() < Configuration.getInstance().getGameWidth() / 2.0) {
            rotatedShooter = new Vector(c.getX() - gunWidth / 2, c.getY(),
                    c.getX() - gunWidth / 2, c.getY() - gunHeight / 2.0);
            rotatedShooter = rotatedShooter.rotateVector(angle);
        } else {
            rotatedShooter = new Vector(c.getX() + gunWidth / 2, c.getY(),
                    c.getX() + gunWidth / 2, c.getY() - gunHeight / 2.0);
            rotatedShooter = rotatedShooter.rotateVector(angle);
        }
        return !(angle > 90) && !(angle < -90) && rotatedShooter.getPositionCoordinate().getX() >= 0 &&
                rotatedShooter.getPositionCoordinate().getX() <= config.getGamePanelDimensions().width;
    }

    /**
     * @return true if the current projectile is an atom
     */
    public boolean projectileIsAtom() {
        return getCurrentProjectile().getSuperType() == SuperType.ATOM;
    }

    /**
     * @return the projectile as an atom
     */
    public Atom getAtomProjectile() {
        return (Atom) getCurrentProjectile();
    }

    @Override
    public String toString() {
        return "Shooter{" +
                "coordinate=" + getCoordinates() +
                ", hitbox=" + getHitbox() +
                ", currentProjectile=" + currentProjectile +
                '}';
    }

    // visitor pattern. Double delegation
    @Override
    public void collideWith(CollisionVisitor visitor, Atom atom) {
        visitor.handleCollision(this, atom);
    }

    @Override
    public void collideWith(CollisionVisitor visitor, Blocker blocker) {
        visitor.handleCollision(this, blocker);
    }

    @Override
    public void collideWith(CollisionVisitor visitor, Molecule molecule) {
        visitor.handleCollision(this, molecule);
    }

    @Override
    public void collideWith(CollisionVisitor visitor, Powerup powerup) {
        visitor.handleCollision(this, powerup);
    }

    @Override
    public void collideWith(CollisionVisitor visitor, Shooter shooter) {
        visitor.handleCollision(this, shooter);
    }

    @Override
    public void acceptCollision(CollisionVisitor visitor, Entity entity) {
        entity.collideWith(visitor, this);
    }
}
