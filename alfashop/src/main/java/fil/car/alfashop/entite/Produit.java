package fil.car.alfashop.entite;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Produit")
public class Produit 
{	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	private String categorie;
	private String nom_prod;
	private String libelle;
	private String description;
	private String marque;
	private double prix;
	private boolean disponible;
	private Date date_pub;
	
	public Produit(String cate,String nom,String lib,String desc,String marq,double prix) {
		this.categorie=cate;
		this.nom_prod = nom;
		this.libelle= lib;
		this.description= desc;
		this.marque= marq;
		this.prix=prix;
	}
	
	
	public int getId_prod() {
		return id;
	}
	public void setId_prod(int id_prod) {
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
 
}
