package ui.windows;

import model.game_entities.enums.EntityType;
import model.game_entities.enums.SuperType;
import model.game_running.GameConstants;
import model.game_space.GameStatistics;
import ui.movable_drawables.ImageResources;

import javax.swing.*;
import java.awt.*;

public class StatisticsWindow extends JPanel implements GameStatistics.GameStatisticsListener {

    //Icons
    private Image atomAlphaImg;
    private Image atomBetaImg;
    private Image atomSigmaImg;
    private Image atomGammaImg;

    private Image powerupAlphaImg;
    private Image powerupBetaImg;
    private Image powerupSigmaImg;
    private Image powerupGammaImg;

    private Image healthImg;
    private Image watchImg;
    private Image blenderImg;

    // JTextFields
    JLabel gammaAtomsNumberLabel;
    JLabel alphaAtomsNumberLabel;
    JLabel betaAtomsNumberLabel;
    JLabel sigmaAtomsNumberLabel;

    JLabel gammaPowerupsNumberLabel;
    JLabel alphaPowerupsNumberLabel;
    JLabel betaPowerupsNumberLabel;
    JLabel sigmaPowerupsNumberLabel;

    JLabel healthLabel;
    JLabel timeLabel;
    JLabel SCORE;
    JLabel scoreLabel;

    public StatisticsWindow() {
        this.setSize(GameConstants.BUILDING_WINDOW_SIZE);
        this.setVisible(true);

        GridBagLayout gridLayout = new GridBagLayout();
        setLayout(gridLayout);

        retrieveImages();
        initializeTextFields();
        setContent();

    }

    private void setContent() {

        GridBagConstraints c = new GridBagConstraints();

        c.ipady = 10;
        //x = 0
        c.weightx = 0.5;
        c.gridx = 0;

        c.gridy = 0;
        add(SCORE, c);
        c.gridy = 1;
        add(new JLabel(new ImageIcon(watchImg)), c);
        c.gridy = 2;
        add(new JLabel(new ImageIcon(healthImg)), c);
        c.gridy = 3;
        add(new JLabel(new ImageIcon(powerupAlphaImg)), c);
        c.gridy = 4;
        add(new JLabel(new ImageIcon(powerupBetaImg)), c);
        c.gridy = 5;
        add(new JLabel(new ImageIcon(powerupGammaImg)), c);
        c.gridy = 6;
        add(new JLabel(new ImageIcon(powerupSigmaImg)), c);
        c.gridwidth = 2;
        c.gridy = 7;
        add(new JLabel(new ImageIcon(blenderImg)), c);
        c.gridwidth = 1;
        c.gridy = 8;
        add(alphaAtomsNumberLabel, c);
        c.gridy = 9;
        add(betaAtomsNumberLabel, c);
        c.gridy = 10;
        add(gammaAtomsNumberLabel, c);
        c.gridy = 11;
        add(sigmaAtomsNumberLabel, c);

        //x = 1
        c.gridx = 1;
        c.gridy = 0;
        add(scoreLabel, c);
        c.gridy = 1;
        add(timeLabel, c);
        c.gridy = 2;
        add(healthLabel, c);
        c.gridy = 3;
        add(alphaPowerupsNumberLabel, c);
        c.gridy = 4;
        add(betaPowerupsNumberLabel, c);
        c.gridy = 5;
        add(gammaPowerupsNumberLabel, c);
        c.gridy = 6;
        add(sigmaPowerupsNumberLabel, c);
        c.gridy = 8;
        add(new JLabel(new ImageIcon(atomAlphaImg)), c);
        c.gridy = 9;
        add(new JLabel(new ImageIcon(atomBetaImg)), c);
        c.gridy = 10;
        add(new JLabel(new ImageIcon(atomGammaImg)), c);
        c.gridy = 11;
        add(new JLabel(new ImageIcon(atomSigmaImg)), c);
    }

    private void initializeTextFields() {
        gammaAtomsNumberLabel = new JLabel("dsfsdfsd");
        alphaAtomsNumberLabel = new JLabel("sdfsdfsd");
        betaAtomsNumberLabel = new JLabel("sdfsdf");
        sigmaAtomsNumberLabel = new JLabel("sdfsdf");

        gammaPowerupsNumberLabel = new JLabel("sdfsdf");
        alphaPowerupsNumberLabel = new JLabel("sdfsdf");
        betaPowerupsNumberLabel = new JLabel("sdfsdf");
        sigmaPowerupsNumberLabel = new JLabel("sdfsdf");

        healthLabel = new JLabel("sdfsdfsd");
        timeLabel = new JLabel("sdfsdfsd");
        SCORE = new JLabel("Score: ");
        scoreLabel = new JLabel("100000000");
    }

    private void retrieveImages() {
        atomAlphaImg = ImageResources.get(EntityType.ALPHA, SuperType.ATOM, GameConstants.ICON_WIDTH, GameConstants.ICON_HEIGHT);
        atomBetaImg = ImageResources.get(EntityType.BETA, SuperType.ATOM, GameConstants.ICON_WIDTH, GameConstants.ICON_HEIGHT);
        atomSigmaImg = ImageResources.get(EntityType.SIGMA, SuperType.ATOM, GameConstants.ICON_WIDTH, GameConstants.ICON_HEIGHT);
        atomGammaImg = ImageResources.get(EntityType.GAMMA, SuperType.ATOM, GameConstants.ICON_WIDTH, GameConstants.ICON_HEIGHT);

        powerupAlphaImg = ImageResources.get(EntityType.ALPHA, SuperType.POWERUP, GameConstants.ICON_WIDTH, GameConstants.ICON_HEIGHT);
        powerupBetaImg = ImageResources.get(EntityType.BETA, SuperType.POWERUP, GameConstants.ICON_WIDTH, GameConstants.ICON_HEIGHT);
        powerupSigmaImg = ImageResources.get(EntityType.SIGMA, SuperType.POWERUP, GameConstants.ICON_WIDTH, GameConstants.ICON_HEIGHT);
        powerupGammaImg = ImageResources.get(EntityType.GAMMA, SuperType.POWERUP, GameConstants.ICON_WIDTH, GameConstants.ICON_HEIGHT);

        healthImg = ImageResources.getIcon(1, GameConstants.ICON_WIDTH, GameConstants.ICON_HEIGHT);
        watchImg = ImageResources.getIcon(2, GameConstants.ICON_WIDTH, GameConstants.ICON_HEIGHT);
        blenderImg = ImageResources.getIcon(3, GameConstants.ICON_WIDTH, GameConstants.ICON_HEIGHT);
    }

    @Override
    public void onHealthChanged(int health) {
        healthLabel.setText(String.valueOf(health));
    }

    @Override
    public void onTimerChanged(String currentTime) {
        healthLabel.setText(currentTime);
    }

    @Override
    public void onScoreChanged(int score) {
        scoreLabel.setText(String.valueOf(score));
    }

    @Override
    public void onProjectileCountChange(SuperType superType, EntityType type, int newCount) {
        String newText = String.valueOf(newCount);
        switch (type) {
            case ALPHA:
                if (superType == SuperType.ATOM)
                    alphaAtomsNumberLabel.setText(newText);
                else
                    alphaPowerupsNumberLabel.setText(newText);
                break;
            case BETA:
                if (superType == SuperType.ATOM)
                    betaAtomsNumberLabel.setText(newText);
                else
                    betaPowerupsNumberLabel.setText(newText);
                break;
            case GAMMA:
                if (superType == SuperType.ATOM)
                    gammaAtomsNumberLabel.setText(newText);
                else
                    gammaPowerupsNumberLabel.setText(newText);
                break;
            case SIGMA:
                if (superType == SuperType.ATOM)
                    sigmaAtomsNumberLabel.setText(newText);
                else
                    sigmaPowerupsNumberLabel.setText(newText);
                break;
        }
    }

    public void update() {
        //todo for later
//        alphaAtomsNumberLabel.setText(tmp.atomMap.get(EntityType.ALPHA).toString());
//        betaAtomsNumberLabel.setText(tmp.atomMap.get(EntityType.BETA).toString());
//        sigmaAtomsNumberLabel.setText(tmp.atomMap.get(EntityType.SIGMA).toString());
//        gammaAtomsNumberLabel.setText(tmp.atomMap.get(EntityType.GAMMA).toString());
//
//        alphaPowerupsNumberLabel.setText(tmp.powerUpMap.get(EntityType.ALPHA).toString());
//        betaPowerupsNumberLabel.setText(tmp.powerUpMap.get(EntityType.BETA).toString());
//        sigmaAtomsNumberLabel.setText(tmp.powerUpMap.get(EntityType.SIGMA).toString());
//        gammaAtomsNumberLabel.setText(tmp.powerUpMap.get(EntityType.GAMMA).toString());
//
//        timeLabel.setText("some text");
//        scoreLabel.setText("some text");
//        healthLabel.setText("some text");
    }
}