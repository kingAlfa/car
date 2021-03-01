package car.projet.entites;

import javax.persistence.*;

@Entity
public class Stock
{

    @Id
    @Column(name="product_id")
    private int id;

    /**
     * La quantite du produit disponible
     */
    private int quantite;

    @OneToOne
    @MapsId
    @JoinColumn(name="product_id")
    private Products products ;

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }
    public int getQuantite() {
        return quantite;
    }


    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
