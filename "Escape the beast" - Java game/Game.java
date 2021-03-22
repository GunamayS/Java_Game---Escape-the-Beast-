import java.util.HashMap;
/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "Escape the beast" is a very simple, text based adventure game.
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Gunamay Sachdev
 * @version 2020.11.24
 */

public class Game 
{
    private Parser parser;
    private Player player;
        
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {        
        Room startRoom = createRooms(); // Creates all the rooms as soon as the game starts
        player = new Player("Player_1", startRoom);        
        player.enterRoom(startRoom); // start game outside
        parser = new Parser();
        
    }

    /**
     * Create all the rooms and link their exits together.
     * 
     * @return Returns the starting room
     */
    private Room createRooms()
    {
        Room entrance, hallway, bathroom, livingRoom, office, kitchen, diningRoom, garden, bedroom, vents;
      
        // create the rooms
        entrance = new Room("inside the entrance of the house you are trapped in");
        hallway = new Room("in the hallway of the house");
        bathroom = new Room("in the bathroom");
        livingRoom = new Room("in the living room");
        office = new Room("in the secret office");
        kitchen = new Room("in the kitchen");
        diningRoom = new Room("in the dining room");
        garden = new Room("in the garden");
        bedroom = new Room("in the bedroom");
        vents = new Room("in the vents");
        
        // put items in the room
        diningRoom.addItem(new Item("bread", "a tasty piece of bread", 0.25));
        vents.addItem(new Item("key", "a mysterious key", 0.1));
        office.addItem(new Item("torch", "a functioning torch", 0.75));
        bedroom.addItem(new Item("apple", "a magic apple", 0.01));
        garden.addItem(new Item("knife", "A pocket knife", 0.5));
        bathroom.addItem(new Item("string", "A piece of nylon string", 0.2));
        livingRoom.addItem(new Item("pen", "A black pen",0.2)); 
        kitchen.addItem(new Item("tablet","An electronic tablet", 5.0));
        
        // initialise room exits
        entrance.setExit("north",hallway);
        
        hallway.setExit("north", diningRoom);
        hallway.setExit("east", livingRoom);
        hallway.setExit("south", entrance);
        hallway.setExit("west", bathroom);
        hallway.setExit("upstairs", bedroom);
        
        bathroom.setExit("east", hallway);
        bathroom.setExit("north", kitchen);
        
        livingRoom.setExit("west", hallway);
        livingRoom.setExit("east", office);
        
        office.setExit("west", livingRoom);
        
        kitchen.setExit("north", garden);
        kitchen.setExit("east", diningRoom);
        kitchen.setExit("south", bathroom);
        
        diningRoom.setExit("south", hallway);
        diningRoom.setExit("west", kitchen);
        
        garden.setExit("south", kitchen);
        
        bedroom.setExit("downstairs", hallway);
        bedroom.setExit("up", vents);
        
        vents.setExit("down", bedroom);

        return entrance;  // start game at the entrance
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
            
            if(player.isDead()) {
                printDead();
                finished = true;
            }
            if(player.howManyItems() == 6) {
                printVictory();
                finished = true;
            }
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to Escape the beast!");
        System.out.println("Escape the beast is a fast-paced game, thrilling game.");
        System.out.println("Your aim is to try and find 6 items spread all over the house that will help you escape.");
        System.out.println("However, a powerful mysterious beast is after you.");
        System.out.println("If you don't find the 6 items within a certain limit of moves, you die...");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println(player.getLongDescription());
    }
    
    /**
     * Prints out the following statements if the player fails to win the game
     */
    private void printDead() 
    {
        System.out.println("\nYou were caught by the beast and lost the game.");
        System.out.println("\nYou didn't collect all 6 items in order to escape.");
    }
    
    /**
     * Prints out the following statements if the player successfuly wins the game
     */
    private void printVictory() 
    {
        System.out.println("\nYou escaped the beast and won the game!");
        System.out.println("\nCongratulations!");
    }

    /**
     * Given a command, process (that is: execute) the command.
     * If this command ends the game, true is returned, otherwise false is
     * returned.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help"))
            printHelp();
        else if (commandWord.equals("go"))
            goRoom(command);
        else if(commandWord.equals("look")) {
            look();
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        } 
        else if (commandWord.equals("take")) {
            take(command);
        }
        else if (commandWord.equals("drop")) {
            drop(command);
        }        
        else if (commandWord.equals("items")) {
            printItems();
        }
        else if (commandWord.equals("eat")) {
            eat(command);
        }
        else if (commandWord.equals("back")){
            goBack(command);
        }
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print a message about the setting and the purpose of the game and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around a haunted house, being chased by an unknown monster.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    /** 
     * Try to go to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = player.getCurrentRoom().getExit(direction);

        if (nextRoom == null)
            System.out.println("There is no door!");
        else {
            player.enterRoom(nextRoom);
            System.out.println(player.getLongDescription());
        }
    }
    
    /**
     * Sends the player back into the last room they were in.
     */
    private void goBack(Command command){
        if(!command.hasSecondWord()){
            Room nextRoom = player.getLastRoom();
            player.enterRoom(nextRoom);
            System.out.println(player.getLongDescription());
        }

    }
    
    /**
     * Prints out the information about the room they are currently in - the items that are located
     * in the room and what exista are available.
     */
    private void look()
    {
        System.out.println(player.getCurrentRoom().getLongDescription());
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game. Return true, if this command
     * quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else
            return true;  // signal that we want to quit
    }
    
    /** 
     * Try to take an item from the current room. If the item is there, pick it up,
     * if not print an error message.
     */
    private void take(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know what to take...
            System.out.println("What do you want to take?");
            return;
        }

        String itemName = command.getSecondWord();
        Item item = player.pickUpItem(itemName);
        
        if(item == null) {
            System.out.println("You can't pick up the item: " + itemName);
            System.out.println("You either don't have enough weight to carry it or you mispelt your input.");
        } else {
            System.out.println("You picked up " + item.getDescription());
        }
    }
    
    /** 
     * Drops an item into the current room. If the player carries the item drop it,
     * if not print an error message.
     */
    private void drop(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know what to drop...
            System.out.println("What do you want to drop?");
            return;
        }

        String itemName = command.getSecondWord();
        Item item = player.dropItem(itemName);
        
        if(item == null) {
            System.out.println("You don't carry the item: " + itemName);
        } else {
            System.out.println("You dropped " + item.getDescription());
        }
    }
    
    /**
     * Prints out the items that the player is currently carrying.
     */
    private void printItems() {
        System.out.println(player.getItemsString());   
    }
    
    /** 
     * Try to take an item from the current room. If the item is there,
     * pick it up, if not print an error message.
     */
    private void eat(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know what to eat
            System.out.println("What do you want to eat?");
            return;
        }
        String itemName = command.getSecondWord();
        Item item = player.eat(itemName);
        if(item == null) {
            System.out.println("You can't eat " + itemName + "!");            
        } 
        else {
            System.out.println("You ate " + item.getDescription());
        }
    }
}
