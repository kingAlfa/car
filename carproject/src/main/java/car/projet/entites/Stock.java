package car.projet.entites;

import javax.persistence.*;

@Entity
public class Stock
{

    @Id
    @Column(name="id")
    private int id;

    /**
     * La quantite du produit disponible
     */
    private int quantite;

    @OneToOne
    @MapsId
    @JoinColumn(name="id")
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
