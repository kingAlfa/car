package fil.car.projet.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Client 
{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id_client;
	private String nom;
	private String prenom;
	private String email;
	private String password;
	private int tel;
	private Date date_enre;
	public int getId_client() {
		return id_client;
	}
	public void setId_client(int id_client) {
		this.id_client = id_client;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getTel() {
		return tel;
	}
	public void setTel(int tel) {
		this.tel = tel;
	}
	public Date getDate_enre() {
		return date_enre;
	}
	public void setDate_enre(Date date_enre) {
		this.date_enre = date_enre;
	}
	public void setPassword(String password) {
		this.password = password;
	}

}
