package ui.movable_drawables;

import model.game_building.Configuration;
import model.game_entities.AutonomousEntity;
import model.game_entities.Entity;
import model.game_entities.Molecule;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * This class is responsible for providing images of entities and icons
 */
public class ImageResources {

    private static final String theme = Configuration.getInstance().getTheme();

    /**
     * @param entity the entity that needs an image to draw itself
     * @return the corresponding image with the specified dimensions
     */
    public static Image get(Entity entity) {
        int width = (int) entity.getHitbox().getWidth();
        int height = (int) entity.getHitbox().getHeight();

        switch (entity.getSuperType()) {
            //Entity is a Blocker, atom, powerup, or molecule, return the corresponding image
            case ATOM:
            case POWERUP:
                AutonomousEntity a = (AutonomousEntity) entity;
                return getImage(a.getSuperType() + "/" + a.getEntityType() + ".png", width, height);

            case BLOCKER:
                AutonomousEntity b = (AutonomousEntity) entity;
                return getImage(b.getSuperType() + "/" + b.getEntityType() + ".png", width,(int)(1.28* height));

            case MOLECULE:
                Molecule m = (Molecule) entity;
                return getImage(m.getSuperType() + "/" + m.getEntityType() + m.getStructure() + ".png", width, height);

            //Entity is a Shooter, return shooter image
            case SHOOTER:
                return getImage("shooter.png", width, height);

            //A default black image will be returned in case of any error
            default:
                System.err.println("Error: ImageResources::get :" + entity.toString() + ", " + width + ", " + height);
                return new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        }
    }


    /**
     * @param type of the icon to be returned
     * @param size that will be used to scale the icon
     * @return the corresponding icon with the specified dimensions
     */
    public static ImageIcon getStatIcon(String folder, String type, int size, boolean bkg) {
        return new ImageIcon(getImage("statisticsIcons" + "/" + folder + "/" + type + bkg + ".png", size, size));
    }

    /**
     * @param name   of the icon to be returned
     * @param width  that will be used to scale the icon
     * @param height that will be used to scale the icon
     * @return the corresponding icon with the specified dimensions
     */
    public static Image get(String name, int width, int height) {
        return getImage(name + ".png", width, height);
    }

    /**
     * @param image  the name of the image to be returned
     * @param width  the width of the image after scaling
     * @param height the height of the image after scaling
     * @return an image to draw in the space
     */
    private static Image getImage(String image, int width, int height) {
        BufferedImage img;
        try {
            img = ImageIO.read(new File(getPath() + image));
        } catch (IOException e) {
            System.err.println("error retrieving image: " + e.getMessage() + " - image: " + image + " - path: " + getPath() + image);
            return new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        }
        return img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    public static Image getGif(String name, int width, int height) {
        return new ImageIcon(getPath() + "gifs/" + name + ".gif", "").
                getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    private static String getPath() {
        return System.getProperty("user.dir") + "/assets/" + theme + "/";
    }

    public static Image backGround(int width, int height) {
        if (theme.equalsIgnoreCase("Disco"))
            return getGif("kuvid_bc", width, height);
        else
            return getImage("kuvid_bc" + ".png", width, height);
    }
}
