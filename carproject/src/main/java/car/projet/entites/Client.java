package car.projet.entites;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Client")
public class Client 
{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	private String nom;
	private String prenom;
	private String email;
	
	private String password;
	private int tel;
	private Date date_enre;
	public Client(String nom,String prenom,String email,String pass,int tel) {
		this.nom=nom;
		this.prenom=prenom;
		this.email=email;
		this.password=pass;
		this.tel=tel;
		//Gerer la date
	}
	
	public Client() {}
	
	public int getId_client() {
		return id;
	}
	public void setId_client(int id_client) {
		this.id = id_client;
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

