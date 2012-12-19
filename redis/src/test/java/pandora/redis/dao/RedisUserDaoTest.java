package pandora.redis.dao;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pandora.redis.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/spring/spring-dao-redis-test.xml" })
public class RedisUserDaoTest extends AbstractJUnit4SpringContextTests {
	@Autowired
	@Qualifier("redisUserDao")
	private RedisUserDao userDao;

	@Test
	public void user() {
		try {
			User user = new User();

			int id = 1000000001;
			String name = "name";

			user.setId(id);
			user.setName(name);

			/*
			 * set
			 */
			userDao.setUser(user);
			User user4redis = userDao.getUser(user);

			Assert.assertEquals("set id", user.getId(), user4redis.getId());
			Assert.assertEquals("set name", user.getName(),
					user4redis.getName());

			/*
			 * chg
			 */
			user.setName("name-chg");
			userDao.chgUser(user);

			user4redis = userDao.getUser(user);

			Assert.assertEquals("chg id", user.getId(), user4redis.getId());
			Assert.assertEquals("chg name", user.getName(),
					user4redis.getName());

			/*
			 * del
			 */
			userDao.delUser(user);
			user4redis = userDao.getUser(user);

			Assert.assertNull("del", user4redis);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
