import greenfoot.*;  // (World, Actor, GreenfootImage, and Greenfoot)
 
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

import java.awt.Font;

/**
 * A space ship. It comes from space, lands, and releases some Greeps into the world.
 * 
 * @author Michael Kolling
 * @author Davin McCall
 * @author Poul Henriksen
 * @version 2.0
 */
public class Ship extends Actor
{
    
    /**
     * Method that creates the Greeps. 
     * You can change the class that objects are created from here.
     */
    private Greep createGreep() 
    {
        if(teamNumber == 1) {
            return new MyGreep(this);
        }
        else {
            return new SimpleGreep(this);
        }        
    }
    
    private int totalPassengers = 20;     // Total number of passengers in this ship.
    private int passengersReleased = 0;   // Number of passengers that left so far.
    private Counter foodCounter;          // Tomato counter 
    private int targetPosition;           // The vertical position for landing
    private int stepCount = 0;
    private boolean hatchOpen = false;    // Whether the greeps can deploy yet    
    // Ship's databank. Holds a large amount of information. = new int[1000];
    private int[] dataBank =
    {   //Map Position
        400, 240, 1,
        400, 360, 2,
        
        100, 305, 3,
        700, 305, 4,
        
        160, 480, 5,
        640, 480, 6,
        
        310, 280, 7,
        490, 290, 8,
       
        // Space
        0,0,0,0,0,0,

        //Paths & Chance
        //1
        400, 50, 260, 40, 15,    //1
        400, 550, 560, 540, 18,  //4
        400, 60, 680, 60, 46,    //2
        400, 530, 100, 530, 100, //3
        0, 0, 0, 0, 0,
        0, 0, 0, 0, 0,
        
        //2
        400, 550, 560, 540, 15,  //4
        400, 50, 260, 40, 18,    //1
        400, 530, 100, 530, 46,  //3
        400, 60, 680, 60, 100,   //2
        0, 0, 0, 0, 0,
        0, 0, 0, 0, 0,
        
        
        //3
        100, 175, 100, 50, 25,   //1
        100, 400, 100, 500, 33,  //4
        290, 400, 400, 300, 100, //3
        0, 0, 0, 0, 0,
        0, 0, 0, 0, 0,
        0, 0, 0, 0, 0,
        
        //4
        700, 175, 700, 50, 25,   //2
        700, 400, 700, 500, 33,  //5
        510, 200, 400, 300, 100, //3
        0, 0, 0, 0, 0,
        0, 0, 0, 0, 0,
        0, 0, 0, 0, 0,
        
        
        //5 
        420, 130, 760, 50, 10,   //2
        380, 190, 750, 550, 11,  //6
        420, 130, 40, 50, 19,    //1
        50, 550, 50, 550, 23,    //4
        350, 281, 400, 220, 50,  //3
        275, 300, 400, 440, 100, //5

        //6
        385, 140, 40, 50, 10,    //1
        420, 195, 50, 550, 11,   //4
        380, 140, 760, 50, 19,   //2
        710, 530, 750, 550, 23,  //6
        480, 270, 400, 220, 50,  //3
        520, 320, 400, 440, 100, //5
      
        
        //7
        90, 240, 385, 52, 50,    //1
        10, 265, 404, 523, 100,  //2
        0, 0, 0, 0, 0,
        0, 0, 0, 0, 0,
        0, 0, 0, 0, 0,
        0, 0, 0, 0, 0,
        
        //8
        730, 240, 385, 52, 50,   //1
        790, 265, 404, 523, 100, //2
        0, 0, 0, 0, 0,
        0, 0, 0, 0, 0,
        0, 0, 0, 0, 0,
        0, 0, 0, 0, 0
        
    };
    
    private int teamNumber; // Team number. Should be 1 or 2.    
    private int direction = 1; // 1 is positive y-direction, -1 is negative.    
    private String greepName; // Name of the Greeps produced by this ship.    
    
    /**
     * Create a space ship. The parameter specifies at what height to land.
     */
    public Ship(String imageName, int position, int teamNumber)
    {
        targetPosition = position;
        this.teamNumber = teamNumber;
        GreenfootImage im = new GreenfootImage(imageName);
        greepName = createGreep().getName();
        setImage(im);
    }
    
    /**
     * Find out which direction we are moving in.
     */
    public void addedToWorld(World w) {
        if(getY() > targetPosition) {
            direction = -1;
        }
        else {
            direction = 1;
        }
    }
    
    /**
     * Let the ship act: move or release greeps.
     */
    public void act()
    {
        if(inPosition() && hatchOpen) {
            if(! isEmpty()) {
                releasePassenger();
            }
        }
        else {
            move();
        }
    }
    
    /**
     * True if all passengers are out.
     */
    public boolean isEmpty()
    {
        return passengersReleased == totalPassengers;
    }
    
    /**
     * Move the ship down or up (for movement before landing).
     */
    public void move()
    {      
        int dist = (targetPosition - getY())  / 16;
        
        if(dist == 0) {
            dist = direction;
        }
        
        setLocation(getX(), getY() + dist);        
        if(inPosition()) {
            // Make sure we are at exactly the right target position
            setLocation(getX(), targetPosition);
        }
    }
    
    /**
     * True if we have reached the intended landing position.
     */
    public boolean inPosition()
    {
        int diff = (getY() - targetPosition) * direction ;
        return diff >= 0;
    }
    
    /**
     * Open the ship's hatch. This allows the greeps to come out.
     */
    public void openHatch()
    {
        hatchOpen = true;
    }
    
    /**
     * Possibly: Let one of the passengers out. Passengers appear at intervals, 
     * so this may or may not release the passenger.
     */
    private void releasePassenger()
    {
        if(passengersReleased < totalPassengers) {
            stepCount++;
            if(stepCount == 10) {
                Greep newGreep = createGreep();
                getWorld().addObject(newGreep, getX(), getY() + 30);
                passengersReleased++;
                stepCount = 0;               
            }
        }
    }

    /**
     * Record that we have collected another tomato.
     */
    public void storeTomato()
    {       
        if(foodCounter == null) {
            foodCounter = new Counter("Tomatoes: ");
            int x = getX();
            int y = getY() + getImage().getHeight() / 2 + 10;
            if(y >= getWorld().getHeight()) {
                y = getWorld().getHeight();    
            }

            getWorld().addObject(foodCounter, x, y);
        }        
        foodCounter.increment();
    }
    
    /**
     * Return the current count of tomatos collected.
     */
    public int getTomatoCount()
    {
        if(foodCounter == null)
            return 0;
        else
            return foodCounter.getValue();
    }
    
    /**
     * Get the ship's data bank array. 
     */
    public int[] getData()
    {
        return dataBank;
    }
    
    /**
     * Return the author name of this ship's Greeps.
     */
    public String getGreepName() 
    {               
        return greepName;
    }
    
    public boolean isTeamTwo() 
    {
        return teamNumber == 2;    
    }   
}
