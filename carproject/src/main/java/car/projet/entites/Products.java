package car.projet.entites;

import java.util.Date;

import javax.persistence.*;

@Entity
//@Table(name="Products")
public class Products 
{	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "id")
	private int id;

	private String categorie;
	private String nom_prod;
	private String libelle;
	private String description;
	private String marque;
	private String urlPhoto ;
	private double prix;
	private boolean disponible;
	private Date date_pub;

	@OneToOne(mappedBy = "products", cascade = CascadeType.ALL)
	@PrimaryKeyJoinColumn
	private Stock stock;

	@OneToOne(mappedBy = "product",cascade = CascadeType.ALL)
	@PrimaryKeyJoinColumn
	private Panier panier;
	/*
	public Products(String cate,String nom,String lib,String desc,String marq,String urlPhoto,double prix) {
		this.categorie=cate;
		this.nom_prod = nom;
		this.libelle= lib;
		this.description= desc;
		this.marque= marq;
		this.prix=prix;
		this.urlPhoto=urlPhoto;
	}
	public Products() {}
	
	*/
	public int getId() {
		return id;
	}
	public void setId(int id_prod) {
		this.id = id_prod;
	}
	public String getCategorie() {
		return categorie;
	}
	public void setCategorie(String categorie) {
		this.categorie = categorie;
	}
	public String getNom_prod() {
		return nom_prod;
	}
	public void setNom_prod(String nom_prod) {
		this.nom_prod = nom_prod;
	}
	public String getLibelle() {
		return libelle;
	}
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getMarque() {
		return marque;
	}
	public String getUrlPhoto() {
		return urlPhoto;
	}
	public void setMarque(String marque) {
		this.marque = marque;
	}
	public double getPrix() {
		return prix;
	}
	public void setPrix(double prix) {
		this.prix = prix;
	}
	public boolean isDisponible() {
		return disponible;
	}
	public void setDisponible(boolean disponible) {
		this.disponible = disponible;
	}
	public Date getDate_pub() {
		return date_pub;
	}
	public void setDate_pub(Date date_pub) {
		this.date_pub = date_pub;
	}
	public void setUrlPhot(String url) {
		this.urlPhoto=url;
	}
 
}
