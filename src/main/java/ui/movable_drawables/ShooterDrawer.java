package ui.movable_drawables;

import model.game_building.Configuration;
import model.game_building.GameConstants;
import model.game_entities.Projectile;
import model.game_entities.Shooter;
import model.game_running.GameCommandListener;
import services.utils.Coordinates;
import services.utils.MathUtils;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This class is responsible for drawing the Shooter given the Shooter entity in the constructor
 */
public class ShooterDrawer implements Drawable, Shooter.ShooterEventListener {

    private final Shooter shooter;
    private final Image shooterBase;
    private final Image shooterImage, shooterImageGif, shootingAnim;
    private final Image belt, beltAnim;
    private Image currentBeltImg;
    private Image currentImg;
    private final Configuration config;
    private final java.util.Timer timer = new Timer();

    public ShooterDrawer(Shooter shooter) {
        this.shooter = shooter;
        this.shooter.setShooterListener(this);
        this.config = Configuration.getInstance();
        this.shooterImage = ImageResources.get(shooter);
        this.shooterBase = ImageResources.get("shooter_base", (int) (shooter.getHitbox().getHeight() * 1.5), (int) shooter.getHitbox().getHeight());
        this.shooterImageGif = ImageResources.getGif("shooter", (int) shooter.getHitbox().getWidth(), (int) shooter.getHitbox().getHeight());
        this.shootingAnim = ImageResources.getGif("shootinganim", (int) shooter.getHitbox().getWidth(), (int) shooter.getHitbox().getHeight());
        this.currentImg = shooterImageGif;
        this.belt = ImageResources.get("belt", (config.getGameWidth()), (int) (0.2 * config.getUnitL()));
        this.beltAnim = ImageResources.getGif("belt", (config.getGameWidth()), (int) (0.2 * config.getUnitL()));
        this.currentBeltImg = belt;

    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform old = g2d.getTransform();

        Projectile projectile = shooter.getCurrentProjectile();

        //get drawing coordinates of shooter
        Coordinates drawingCoordinates = MathUtils.drawingCoordinates(
                shooter.getCoordinates(),
                shooter.getHitbox().getWidth(),
                shooter.getHitbox().getHeight());


        //draw base if the theme is disco theme
        if (config.isDiscoTheme()) {
            //draw belt
            g.drawImage(currentBeltImg, (int) (0.2 * config.getUnitL()), (int) (config.getGameHeight() - config.getUnitL() * 0.45), null);

            g2d.drawImage(shooterBase,
                    (int) (shooter.getCoordinates().getX() - 0.5 * shooterBase.getWidth(null)),
                    (int) (config.getGameHeight() - config.getUnitL() * 1.25),
                    null);
        }

        //rotate the graphics according to shooter angle
        g2d.rotate(Math.toRadians(
                shooter.getAngle()),
                (shooter.getCoordinates().getPoint().x),
                (int) (shooter.getCoordinates().getPoint().y + shooter.getHitbox().getHeight() * 0.25));

        //draw shooter image
        g2d.drawImage(
                currentImg,
                drawingCoordinates.getPoint().x,
                drawingCoordinates.getPoint().y,
                null);

        //draw projectile
        if (projectile != null) {
            Coordinates projectileCoord = MathUtils.drawingCoordinates(
                    shooter.getCoordinates(),
                    0, projectile.getHitbox().getHeight() + shooter.getHitbox().getHeight());
            projectile.setCoordinates(projectileCoord);
            DrawableFactory.get(projectile).draw(g2d);
        }

        g2d.setTransform(old);
    }

    public void drawHitbox(Graphics g) {
        Coordinates drawingCoordinates = MathUtils.drawingCoordinates(shooter.getCoordinates(),
                shooter.getHitbox().getWidth(),
                shooter.getHitbox().getHeight());

        g.drawRect(
                drawingCoordinates.getPoint().x,
                drawingCoordinates.getPoint().y,
                (int) shooter.getHitbox().getWidth(),
                (int) shooter.getHitbox().getHeight());
    }

    @Override
    public void onShot() {
        if (config.isDiscoTheme()) {
            currentImg = shootingAnim;
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    currentImg = shooterImageGif;
                }
            }, GameConstants.DEFAULT_SHOOTER_DELAY);
        }
    }

    @Override
    public void onStopped() {
        if (config.isDiscoTheme())
            currentBeltImg = belt;
    }

    @Override
    public void onMoved() {
        if (config.isDiscoTheme())
            currentBeltImg = beltAnim;
    }
}
