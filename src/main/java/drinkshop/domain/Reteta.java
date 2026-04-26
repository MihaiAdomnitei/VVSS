package drinkshop.domain;

import java.util.List;

public class Reteta {

    private int id;
    private List<IngredientReteta> ingrediente;

    public Reteta(int id, List<IngredientReteta> ingrediente) {
        this.id = id;
        this.ingrediente = new java.util.ArrayList<>(ingrediente);  // Defensive copy
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }


    public List<IngredientReteta> getIngrediente() {
        return new java.util.ArrayList<>(ingrediente);  // Defensive copy
    }

    public void setIngrediente(List<IngredientReteta> ingrediente) {
        this.ingrediente = new java.util.ArrayList<>(ingrediente);  // Defensive copy
    }

    @Override
    public String toString() {
        return "Reteta{" +
                "productId=" + id +
                ", ingrediente=" + ingrediente +
                '}';
    }
}

