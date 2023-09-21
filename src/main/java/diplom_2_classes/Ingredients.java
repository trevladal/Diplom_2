package diplom_2_classes;

import java.util.ArrayList;
import java.util.List;

public class Ingredients {

    public List<String> ingredients;
    public Ingredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public Ingredients() {
    }

    public void addIngredient(String ingredient) {
        ingredients.add(ingredient);
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public static Ingredients empty () {
        return new Ingredients(new ArrayList<>());
    }
}
