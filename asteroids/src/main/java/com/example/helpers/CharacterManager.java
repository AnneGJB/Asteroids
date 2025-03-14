package com.example.helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.example.AsteroidsApplication;
import com.example.characters.Asteroid;
import com.example.characters.Ship;

import javafx.scene.shape.Polygon;

import com.example.characters.Character;
import com.example.characters.Projectile;

public class CharacterManager {
    private int frameWidth;
    private int frameHeight;

    private Ship ship;
    List<Character> asteroids;
    List<Character> projectiles;

    public CharacterManager() {
        this.frameWidth = AsteroidsApplication.WIDTH;
        this.frameHeight = AsteroidsApplication.HEIGHT;
        this.ship = new Ship(this.frameWidth / 2, this.frameHeight / 2);
        this.asteroids = new ArrayList<>();
        this.projectiles = new ArrayList<>();
        generateStartingAsteroids();
    }

    private void generateStartingAsteroids() {
        for (int i = 0; i < 5; i++) {
            Random random = new Random();
            Asteroid asteroid = new Asteroid(random.nextInt(this.frameWidth / 3), random.nextInt(this.frameHeight));
            this.asteroids.add(asteroid);
        }
    }

    public Projectile generateProjectile() {
        Projectile projectile = new Projectile(
                (int) getShipCharacter().getTranslateX(),
                (int) getShipCharacter().getTranslateY());

        projectile.getCharacter().setRotate(getShipCharacter().getRotate());
        this.projectiles.add(projectile);

        projectile.accelerate();
        projectile.setMovement(projectile.getMovement().normalize().multiply(3));

        return projectile;
    }

    public void moveAllCharacters() {
        this.ship.move();
        this.asteroids.forEach(asteroid -> asteroid.move());
        this.projectiles.forEach(projectile -> projectile.move());
    }

    public boolean shipHasCollided() {
        for (Character asteroid : this.asteroids) {
            if (this.ship.collide(asteroid)) {
                return true;
            }
        }
        return false;
    }

    public List<Polygon> getProjectileCollision() {
        for (Character projectile : this.projectiles) {
            for (Character asteroid : this.asteroids) {
                if (projectile.collide(asteroid)) {
                    this.projectiles.remove(projectile);
                    this.asteroids.remove(asteroid);
                    return List.of(projectile.getCharacter(), asteroid.getCharacter());
                }
            }
        }
        return Collections.emptyList();
    }

    public Polygon generateRandomAsteroid() {
        if (Math.random() < 0.005) {
            Asteroid asteroid = new Asteroid(this.frameWidth, this.frameHeight);
            if (!asteroid.collide(this.ship)) {
                this.asteroids.add(asteroid);
                return asteroid.getCharacter();
            }
        }
        return null;
    }

    public void turnShipLeft() {
        this.ship.turnLeft();
    }

    public void turnShipRight() {
        this.ship.turnRight();
    }

    public void accelerateShip() {
        this.ship.accelerate();
    }

    public void decelerateShip() {
        this.ship.decelerate();
    }

    public Polygon getShipCharacter() {
        return this.ship.getCharacter();
    }

    public List<Polygon> getAsteroidCharacters() {
        return this.asteroids.stream()
                .map(asteroid -> asteroid.getCharacter())
                .collect(Collectors.toList());
    }

    public List<Character> getProjectiles() {
        return this.projectiles;
    }

}
