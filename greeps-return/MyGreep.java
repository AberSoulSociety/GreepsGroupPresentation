import greenfoot.*;  // (World, Actor, GreenfootImage, and Greenfoot)

/**
 * A Greep is an alien creature that likes to collect tomatoes.
 * 
 * Rules:
 * 
 * Rule 1 
 * Only change the class 'MyGreep'. No other classes may be modified or created. 
 *
 * Rule 2 
 * You cannot extend the Greeps' memory. That is: you are not allowed to add 
 * fields (other than final fields) to the class. Some general purpose memory is
 * provided. (The ship can also store data.) 
 * 
 * Rule 3 
 * You can call any method defined in the "Greep" superclass, except act(). 
 * 
 * Rule 4 
 * Greeps have natural GPS sensitivity. You can call getX()/getY() on any object
 * and get/setRotation() on yourself any time. Friendly greeps can communicate. 
 * You can call getMemory() and getFlag() on another greep to ask what they know. 
 * 
 * Rule 5 
 * No creation of objects. You are not allowed to create any scenario objects 
 * (instances of user-defined classes, such as MyGreep). Greeps have no magic 
 * powers - they cannot create things out of nothing. 
 * 
 * Rule 6 
 * You are not allowed to call any methods (other than those listed in Rule 4)
 * of any other class in this scenario (including Actor and World). 
 *  
 * If you change the name of this class you should also change it in
 * Ship.createGreep().
 * 
 * Please do not publish your solution anywhere. We might want to run this
 * competition again, or it might be used by teachers to run in a class, and
 * that would be ruined if solutions were available.
 * 
 * 
 * @author (your name here)
 * @version 0.1
 */
public class MyGreep extends Greep
{
    // Remember: you cannot extend the Greep's memory. So:
    // no additional fields (other than final fields) allowed in this class!
    
    
    int[] data = 
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
        400, 530, 120, 530, 50,
        400, 60, 670, 60, 50,
        400, 50, 270, 40, 50,
        400, 550, 550, 540, 50,
        400, 530, 120, 530, 50,
        400, 60, 670, 60, 50,
        
        
        400, 60, 670, 60, 50,
        400, 530, 120, 530, 50,
        400, 550, 550, 540, 50,
        400, 50, 270, 40, 50,
        400, 60, 670, 60, 50,
        400, 530, 120, 530, 50
        
    };
    
    /**
     * Default constructor. Do not remove.
     */
    public MyGreep(Ship ship)
    {
        super(ship);
    }
    
    /**
     * Do what a greep's gotta do.
     */
    public void act()
    {
        super.act();   // do not delete! leave as first statement in act().
        
         if (isAtShipWithTomato()) dropTomato();
        //figurePosition();
         if (getMemory(0) == 0)figurePosition();
        
         if(atShip()) setGreep();
        
        
        setFlag();
        if (!getFlag(1) && !getFlag(2)) turnTowards(resolveX(getMemory(1)), resolveY(getMemory(1)));
        if (!getFlag(1) && getFlag(2)) turnTowards(resolveX(getMemory(2)), resolveY(getMemory(2)));
        if (getFlag(1) && !getFlag(2)) turnTowards(resolveX(getMemory(1)), resolveY(getMemory(1)));
        if ((getFlag(1) && getFlag(2)) || getMemory(1)==0 || getMemory(2)==0) turnHome();
        
       checkFood();
       move();
    }
    
    /** 
     * Move forward, with a slight chance of turning randomly
     */
    public void randomWalk()
    {
        // there's a 3% chance that we randomly turn a little off course
        if (randomChance(3)) {
            turn((Greenfoot.getRandomNumber(3) - 1) * 100);
        }
        
        move();
    }

    /**
     * Is there any food here where we are? If so, try to load some!
     */
    public void checkFood()
    {
        // check whether there's a tomato pile here
        TomatoPile tomatoes = getTomatoes();
        if(tomatoes != null && !carryingTomato()) {
            loadTomato();
            // Note: this attempts to load a tomato onto *another* Greep. It won't
            // do anything if we are alone here.
            turnTowards(tomatoes.getX(), tomatoes.getY());
        }
    }

    /**
     * This method specifies the name of the greeps (for display on the result board).
     * Try to keep the name short so that it displays nicely on the result board.
     */
    public String getName()
    {
        return "Soul Society!";  // write your name here!
    }
    
    
    
    public boolean isAtShipWithTomato()
    {
        if (atShip() && carryingTomato()) return true;
        return false;
    }
    
    public boolean isAtShipWithoutTomato()
    {
        if (atShip() && !carryingTomato()) return true;
        return false;
    }
    
    
    public boolean isInZoneAroundXY(int X, int Y)
    {
        if (getX()> X -20 && getX() < X +20 && getY() > Y-20 && getY() < Y +20) return true;
        return false;
    }
    
    public boolean isInZoneWithTomato(int X, int Y)
    {
        if (isInZoneAroundXY(X, Y) && carryingTomato()) return true;
        return false;
    }
    
    public boolean isInZoneWithoutTomato(int X, int Y)
    {
        if (isInZoneAroundXY(X, Y) && !carryingTomato()) return true;
        return false;
    }
    
    public void setFlag()
    {
        if (isAtShipWithoutTomato())
        {
            setFlag(1, false);
            setFlag(2, false);
        }
        else if (isInZoneWithoutTomato(resolveX(getMemory(1)), resolveY(getMemory(1))))
        {
           setFlag(1, false);
           setFlag(2, true);
        }
        else if (isInZoneWithoutTomato(resolveX(getMemory(2)), resolveY(getMemory(2))))
        {
           setFlag(1, true);
           setFlag(2, false);
        }
        else if (isInZoneWithTomato(resolveX(getMemory(1)), resolveY(getMemory(1))))
        {
           setFlag(1, true);
           setFlag(2, true);
        }
        
    }
    
    public void setGreep()
    {
        final int block = 30;
        
        if (randomChance(data[(getMemory(0)* block) + 4]))
        {
            setMemory(1, mergeXY(data[(getMemory(0)* block) +0],data[(getMemory(0)* block) +1]));
            setMemory(2, mergeXY(data[(getMemory(0)* block) +2],data[(getMemory(0)* block) +3]));
        }
        else if (randomChance(data[(getMemory(0)* block) + 9]))
        {
          setMemory(1, mergeXY(data[(getMemory(0)* block) +5],data[(getMemory(0)* block) +6]));
          setMemory(2, mergeXY(data[(getMemory(0)* block) +7],data[(getMemory(0)* block) +8]));
        } 
        else if (randomChance(data[(getMemory(0)* block) + 14]))
        {
          setMemory(1, mergeXY(data[(getMemory(0)* block) +10],data[(getMemory(0)* block) +11]));
          setMemory(2, mergeXY(data[(getMemory(0)* block) +12],data[(getMemory(0)* block) +13]));
        }
        else if (randomChance(data[(getMemory(0)* block) + 19]))
        {
          setMemory(1,mergeXY(data[(getMemory(0)* block) +15],data[(getMemory(0)* block) +16]));
          setMemory(2,mergeXY(data[(getMemory(0)* block) +17],data[(getMemory(0)* block) +18])); 
        } 
        else if (randomChance(24))
        {
          setMemory(1,mergeXY(data[(getMemory(0)* block) +20],data[(getMemory(0)* block) +21]));
          setMemory(2,mergeXY(data[(getMemory(0)* block) +22],data[(getMemory(0)* block) +23])); 
        }
        else
        {
          setMemory(1,mergeXY(data[(getMemory(0)* block) +25],data[(getMemory(0)* block) +26]));
          setMemory(2,mergeXY(data[(getMemory(0)* block) +27],data[(getMemory(0)* block) +28])); 
        }
    }
    
    
    public int mergeXY(int X, int Y)
    {
        return (800*X) + Y;
    }
    
    public int resolveX(int code)
    {
        return code/800;
    }
    
    public int resolveY(int code)
    {
        return code%800;
    }
    
    
    public void figurePosition()
    {   
        for (int index = 0; index <24; index += 3)
            if(isInZoneAroundXY(data[index],data[index+1])) {setMemory(0, data[index+2]); return;}
    }
}
