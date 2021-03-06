package car.projet.dao;

import car.projet.entites.Login;
import car.projet.entites.Users;



public interface UserDao
{
    void register(Users user);
    Users validateUser(Login login);
}
