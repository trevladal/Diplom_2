package diplom_2_classes;

import com.google.gson.annotations.SerializedName;

public class Ingredient {
    @SerializedName("_id")
    public String hash;

    public Ingredient() {
    }

    public Ingredient(String hash) {
        this.hash = hash;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
