package model.game_space;


import model.game_building.GameConstants;
import model.game_running.ProjectileContainer;

import java.util.logging.Level;
import java.util.logging.Logger;


public class Blender {

    private Logger logger = Logger.getLogger(Blender.class.getName());

    private final ProjectileContainer projectileContainer;
    private BlenderListener blenderListener;

    public Blender(BlenderListener blenderListener, ProjectileContainer projectileContainer) {
        this.projectileContainer = projectileContainer;
        this.blenderListener = blenderListener;
        logger.setLevel(Level.OFF);
    }

    /**
     * Blends/Breaks a number of source atoms into a number of target atoms
     *
     * @param sourceAtom              The atom to be blended
     * @param destinationAtom         The result atom
     * @param destinationAtomQuantity The number of the desired atom.
     */
    public void blend(int sourceAtom, int destinationAtom, int destinationAtomQuantity) {
        boolean canBlend;
        canBlend = projectileContainer.decreaseAtoms(sourceAtom, destinationAtomQuantity *
                (int) Math.ceil(sourceAtom * GameConstants.BLENDING_MATRIX[sourceAtom - 1][destinationAtom - 1]));
        if (canBlend) {
            projectileContainer.increaseAtoms(destinationAtom, destinationAtomQuantity *
                    (int) Math.ceil(destinationAtom * GameConstants.BLENDING_MATRIX[destinationAtom - 1][sourceAtom - 1]));
            if (blenderListener != null)
                blenderListener.onBlend();
        } else {
            if (blenderListener != null)
                blenderListener.onFailBlend();
        }
    }

    public void showBlender() {
        blenderListener.onShow();
    }

    public void setBlenderListener(BlenderListener blenderListener) {
        this.blenderListener = blenderListener;
    }

    public interface BlenderListener {
        /**
         * this method is called after game parameters get checked and proved valid.
         */
        void onBlend();
        void onFailBlend();
        void onShow();
    }
}
