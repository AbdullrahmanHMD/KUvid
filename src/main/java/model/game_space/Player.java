package model.game_space;

import com.fasterxml.jackson.annotation.JsonIgnore;
import model.game_entities.enums.ShieldType;
import model.game_running.ShieldHandler;

//todo: can be a controller for shooter, projectile container, and blender.
public class Player {
    private String username;
    private int health; //might be double?
    private double score;
    private GameTimer timer;
    private GameStatistics statistics;

    public Player(String username, GameStatistics.GameStatisticsListener statisticsListener) {
        this.username = username;
        health = 100;
        score = 0;
        timer = new GameTimer(10);
        statistics = new GameStatistics(statisticsListener);
    }

    public Player() {
        timer = new GameTimer(10);
        statistics = new GameStatistics(null); // listener to be set later
    }

    public void setStatisticsListener(GameStatistics.GameStatisticsListener listener) {
        statistics.setStatisticsListener(listener);
        statistics.updateHealth(health);
        statistics.updateTimer(timer.getCurrentTimer());
        statistics.updateScore(score);
    }

    public String getUsername() {
        return username;
    }

    public int getHealth() {
        return health;
    }

    public double getScore() {
        return score;
    }

    public GameTimer getTimer() {
        return timer;
    }

    @JsonIgnore
    public GameStatistics.GameStatisticsListener getStatisticsListener(){
        return this.statistics.getStatisticsListener();
    }

    public void updateOwnedProjectiles() {
        statistics.changeProjectileCount();
    }


    public void changeShieldCount(){
        statistics.changeShieldCount();
    }

    public boolean loseHealth(int damageAmount) {
        health -= damageAmount;
        if (health < 0)
            health = 0;
        statistics.updateHealth(health);
        return health <= 0;
    }

    public boolean updateTime(int amountInMillis) {
        timer.decrease(amountInMillis);
        statistics.updateTimer(timer.getCurrentTimer());
        return timer.getRemainingTimeMillis() >= 1;
    }

    public void incrementScore(double scoreIncrement) { //assuming the score will only be incremented by 1.
        this.score += scoreIncrement;
        statistics.updateScore(this.score);
    }

    @Override
    public String toString() {
        return "Player{" +
                "username='" + username + '\'' +
                ", health=" + health +
                ", score=" + score +
                ", timer=" + timer +
                '}';
    }
}
