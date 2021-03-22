/**
 * Class Item - an item in an adventure game.
 *
 * This class contains all the characteristics of each item, such as its name, weight etc.
 * 
 * An  "Item" has a description and a weight.
 * 
 * @author  Gunamay Sachdev
 * @version 2020.11.24
 */
public class Item
{
    // The name of the item.
    private String name;
    // A description of the item.
    private String description;
    // The weight of the item.
    private double weight;
    
    /**
     * Create a new item with the given description and weight.
     * @param name The item's name
     * @param description The item's description
     * @param weight The item's weight
     */
    public Item(String name, String description, double weight)
    {
        this.name = name;
        this.description = description;
        this.weight = weight;
    }

    /**
     * Return the item's name.
     * @return The name
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * Return the item's weight.
     * @return The weight
     */
    public double getWeight()
    {
        return weight;
    }
    
    /**
     * Return a description of the item.
     * @return The description.
     */
    public String getDescription()
    {
        return description;
    }
}