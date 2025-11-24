
/**
 * <p> Represents an item that can be stored in the inventory or sold in a shop.
 * Each object includes data such as name, type, quantity, price, and stats. </p>
 * @author Chelsea Ye (cye68)
 */
public class InventoryObject {
    protected int amount;
    protected int price;
    protected String type;
    protected String name;
    protected int stats;

    /**
     * <p> Constructs an InventoryObject (can be food or gift) with the given properties. </p>
     *
     * @param name  the name of the item
     * @param type  the type/category of the item
     * @param qty   the items quantity
     * @param price the item's price
     * @param stats the fullness/happiness that the item adds
     */
    public InventoryObject(String name, String type, int qty, int price, int stats){
        this.name = name;
        this.type = type;
        this.amount = qty;
        this.price = price;
        this.stats = stats; 
    }
    
    /** @return the price of the item */
    public int getPrice() {
        return price;
    }

    /** @return the quantity of the item */
    public int getAmount(){
        return amount;
    }

    /** @return the name of the item */
    public String getName() {
        return name;
    }

    /** @return the type/category of the item */
    public String getType() {
        return type;
    }

    /** @return the item's additional stat value */
    public int getStats() {
        return stats;
    }

    /**
     * Sets the quantity of the item.
     *
     * @param qty the new item quantity
     */
    public void setQty(int qty) {
        amount = qty;
    }

    /**
     * Returns a string representation of the InventoryObject atributes in csv format.
     *
     * @return a string of the InventoryObject atributes in csv format.
     */
    public String toString() {
        return String.format("Item{name='%s',type='%s',quantity=%d,price=%d,stats=%d}", name, type, amount, price, stats);
    }

}
