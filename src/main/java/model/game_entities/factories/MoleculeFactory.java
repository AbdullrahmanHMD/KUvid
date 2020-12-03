package model.game_entities.factories;

import model.game_entities.Molecule;

import model.game_entities.enums.MoleculeStructure;
import model.game_entities.enums.MoleculeType;

import model.game_physics.hitbox.HitboxFactory;
import model.game_physics.path_patterns.PathPatternFactory;
import utils.Coordinates;

public class MoleculeFactory {
    private static MoleculeFactory moleculeFactory = new MoleculeFactory();

    private MoleculeFactory(){
    }

    public static MoleculeFactory getInstance(){
        if(moleculeFactory == null){
            moleculeFactory = new MoleculeFactory();
        }
        return moleculeFactory;
    }

    public Molecule getMolecule(int i){// TODO : modify implementation.
        Coordinates defaultCoordinates = new Coordinates(0,0);

        switch (i){
            case 0:
                return new Molecule(defaultCoordinates, HitboxFactory.getInstance().getMoleculeHitbox(),
                        PathPatternFactory.getInstance().getMoleculePathPattern(), MoleculeType.ALPHA_,
                        MoleculeStructure.CIRCULAR);
            case 1:
                return new Molecule(defaultCoordinates, HitboxFactory.getInstance().getMoleculeHitbox(),
                        PathPatternFactory.getInstance().getMoleculePathPattern(), MoleculeType.BETA_,
                        MoleculeStructure.CIRCULAR);
            case 2:
                return new Molecule(defaultCoordinates, HitboxFactory.getInstance().getMoleculeHitbox(),
                        PathPatternFactory.getInstance().getMoleculePathPattern(), MoleculeType.GAMMA_,
                        MoleculeStructure.CIRCULAR);
            case 3:
                return new Molecule(defaultCoordinates, HitboxFactory.getInstance().getMoleculeHitbox(),
                        PathPatternFactory.getInstance().getMoleculePathPattern(), MoleculeType.SIGMA_,
                        MoleculeStructure.CIRCULAR);
            default:
                return null;
        }
    }
}


