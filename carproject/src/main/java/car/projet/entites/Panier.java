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

   // @OneToOne
   // @MapsId
   // @JoinColumn(name="id")
   // private Products product;

    /**
     * Je vais associer un utilisateur à chaque ligne du panier
     * Comme ça j'ai acces à tous les produits correspondant à un utilisateur
     */
   /* private int id_User;

    public int getId_User() {
        return id_User;
    }

    public void setId_User(int id_User) {
        this.id_User = id_User;
    }
*/


    public Panier(){}
    public Panier(int id,int quantite ){
        this.id=id;
        this.quantite=quantite;
    }
   // public void setProduct(Products products){this.product=products;}
    public int getQuantite() {
        return quantite;
    }
    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public void updateQuantite(int quantite){
        this.quantite+=quantite;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
