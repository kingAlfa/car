package car.projet.dao;

import car.projet.entites.Login;
import car.projet.entites.Users;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class UserDaoImp implements UserDao
{
    @Autowired
    DataSource dataSource;
    @Autowired
    JdbcTemplate jdbcTemplate;


    @Override
    public void register(Users user) {
        String sql = "insert into users(id,username,password,firstname,lastname,email,adress,phone) values (?,?,?,?,?,?,?,?)";
        jdbcTemplate.update(sql, user.getId(),user.getUsername(),user.getPassword(),user.getFirstname(),
                user.getLastname(),user.getEmail(),user.getAdress(),user.getPhone());
    }

    @Override
    public Users validateUser(Login login) {
        String sql = "select * from users where username='"+login.getUsername()+"'and password='"+login.getPassword()+"'";
        List<Users> users = jdbcTemplate.query(sql,new UserMapper());
        return users.size() >0 ? users.get(0) : null;
    }
}

class UserMapper implements RowMapper<Users> {

    @Override
    public Users mapRow(ResultSet rs, int rowNum) throws SQLException {
        Users user = new Users();
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setFirstname(rs.getString("firstname"));
        user.setLastname(rs.getString("lastname"));
        user.setEmail(rs.getString("email"));
        user.setAdress(rs.getString("adress"));
        user.setPhone(rs.getInt("phone"));


        return user;
    }
}
