package car.projet.dao;

import car.projet.entites.Users;
import org.springframework.data.repository.Repository;

public interface UserRepository extends Repository<Users,Integer>
{
    Users findByUsername(String username);
    Users findById(int id);

}
