package pandora.redis.dao;

import java.sql.SQLException;

import pandora.redis.domain.User;

public interface RedisUserDao {
	public void setUser(User user) throws Exception;

	public User getUser(User user) throws SQLException;

	public void chgUser(User user) throws Exception;

	public void delUser(User user) throws Exception;
}
