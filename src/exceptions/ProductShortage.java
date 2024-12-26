package exceptions;

public class ProductShortage extends ApplicationException {
    public ProductShortage(String quantity, String product) {

        super("Il ne reste que " + quantity + " unités du produit " + product, new Throwable("Product Shortage"));
    }
}
