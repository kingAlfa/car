Drop table Products if exists ;
Create table Products(
  id INTEGER   PRIMARY KEY,
  categorie VARCHAR(50) ,
  nom VARCHAR(30),
  Description VARCHAR(100),
  marque VARCHAR(30),
  url_photo VARCHAR(250),
  prix numeric,
  date_publi Date
) ;
insert into Products values(1,
							'informatique',
							'Dell E1232',
							'Ici la description',
							'Dell',
							'/images/merci.gif'
							,23,
							'2021-02-23') ;