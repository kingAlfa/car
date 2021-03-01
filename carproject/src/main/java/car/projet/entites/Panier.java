package car.projet.entites;

import javax.persistence.*;

@Entity
public class Panier
{
    @Id
    @Column(name="id")
    private int id;


    /**
     * La quantite de chaque produit dans le panier
     */
    private int quantite;

    @OneToOne
    @MapsId
    @JoinColumn(name="id")
    Products products;

    public Panier(){}
    public Panier(int id,int quantite){
        this.id=id;
        this.quantite = quantite;
    }

    public int getQuantite() {
        return quantite;
    }
    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }
}
