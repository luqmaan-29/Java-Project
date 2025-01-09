import java.util.*;

public class Cart {
    private List<Map<String, Object>> cartItems;

    public Cart() {
        cartItems = new ArrayList<>();
    }

    public void addToCart(Map<String, Object> medicine, int quantity) {
        Map<String, Object> cartItem = new HashMap<>(medicine);
        cartItem.put("quantity", quantity);
        cartItems.add(cartItem);
    }

    public List<Map<String, Object>> getCartItems() {
        return cartItems;
    }
}
