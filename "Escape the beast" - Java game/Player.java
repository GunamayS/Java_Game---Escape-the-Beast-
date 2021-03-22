/**
 * This is the representation of a player in the game Escape the beast.
 * 
 * It holds all the information and methods about the player in this class such as the items
 * is carrying and the number of moves the player has performed.
 * 
 * @author  Gunamay Sachdev
 * @version 2020.11.24
 */

public class Player
{
    // The player's name.
    private String name;
    // The room the player is in.
    private Room currentRoom;
    // The item's the player is holding.
    private Items items = new Items();
    // The maximum weight the player can hold.
    private double maxWeight;
    // The number of moves the player has used.
    private int moves = 0;
    // The limit of moves the player has.
    private int maxMoves = 15;
    // The previous room the player was in prior to moving into a new room
    private Room lastRoom;
    
    /**
     * Constructor for objects of class Player
     * @param name The player's name
     * @param start The room the player is in
     */
    public Player(String name, Room start)
    {
        this.name = name;
        this.maxWeight = 1.0;//Sets the maximum weight a player can carry to 1.0
        this.currentRoom = start;
        this.lastRoom = start;
    }

    /**
     * Enter the given room.
     * @param room The room entered.
     */
    public void enterRoom(Room room)
    {
        moves++; // increments the number of moves the player has performed when moving into a new room
        lastRoom = currentRoom; // sets the current room as the last room
        currentRoom = room;// sets the new room that the player is to enter into as the current room
    }
    
    /**
     * Gets the room in which the player is currently located.
     * @return The current room.
     */
    public Room getCurrentRoom()
    {
        return currentRoom;
    }
    
    /**
     * Gets the room in which the player last was prior to entering their current location.
     * @return The last room. 
     */
    public Room getLastRoom()
    {   
        return lastRoom;
    }
    
    /**
     * Get the name of the player.
     * @return The player's name
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * Returns a string describing the items that the player carries.
     * @return A description of the items held.
     */
    public String getItemsString()
    {
        return "You are carrying: " + items.getLongDescription();
    }
    
    /**
     * Returns a string describing the players current location and which
     * items the player carries.
     * @return A description of the room and items held.
     */
    public String getLongDescription()
    {       
        String returnString = currentRoom.getLongDescription();
        returnString += "\n" + getItemsString();
        return returnString;
    }
    
        /**
     * Checks if we can pick up the given item. This depends on whether the item 
     * actually is in the current room and if it is not too heavy.
     * @parem itemName The item to be picked up.
     * @return true if the item can be picked up, false otherwise.
     */
    private boolean canPickItem(String itemName)
    {
        boolean canPick = true;
        Item item = currentRoom.getItem(itemName);// gets the items in the current room
        if(item == null) {
            canPick = false;
        }
        else {
            double totalWeight = items.getTotalWeight() + item.getWeight();
            if(totalWeight > maxWeight) {
                canPick = false;
            }
        }
        return canPick;         
    }
    

    /**
     * Tries to pick up the item from the current room.
     * @param itemName The item to be picked up.
     * @return If successful, this method will return the item that was picked up.
     */
    public Item pickUpItem(String itemName)
    {
        if(canPickItem(itemName)) {
            Item item = currentRoom.removeItem(itemName);
            items.put(itemName, item);            
            return item;
        } 
        else {
            return null;
        }
    }
    
    
    /**
     * Tries to drop an item into the current room.
     * @param itemName The item to be dropped.
     * 
     * @return If successful this method will return the item that was dropped.
     */
    public Item dropItem(String itemName)
    {
        Item item = items.remove(itemName);
        if(item != null) {
            currentRoom.addItem(item);            
        }
        return item;
    }
    
    /**
     * Eats the item if possible.
     * Only apples can be eaten.
     * @param itemName The item to be eaten.
     */
    public Item eat(String itemName)
    {
        if(itemName.equals("apple")) {
            //First see if we have a apple in our inventory
            Item apple = items.get(itemName);
            //Then check if there is a apple in the room
            if(apple == null) { 
                apple = currentRoom.removeItem(itemName);
            }
            if(apple != null) {
                maxWeight += 1;
                System.out.println("The weight limit of the items you can carry has doubled!");
                System.out.println("You can now drop your magic apple and find the rest of the items!");
                System.out.println("Hurry! The beast is close to you!");
                return apple;    
            }
        }
        return null;
    }
        
    /**
     * Checks if the player is dead. 
     * The player dies when he has exceeded some number of moves.
     * @return true if the moves the player has used has exceeded the limit of moves.
     */
    public boolean isDead() 
    {
        return moves > maxMoves;
    }
    
    /**
     * Checks how many items the player has on them. 
     * @return the number of items they are carrying
     */
    public int howManyItems(){
        return items.getNumberOfItems();
    }

}