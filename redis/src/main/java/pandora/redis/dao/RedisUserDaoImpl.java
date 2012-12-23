package pandora.redis.dao;

/*
 * Copyright guriguri(guriguri.kr@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import java.sql.SQLException;
import java.util.Map;

import pandora.domain.User;

import redis.clients.jedis.ShardedJedis;

public class RedisUserDaoImpl extends BaseRedisDao implements RedisUserDao {
	private static final String TABLE_PREFIX = "USER:";
	private static final String USER_ID = "id";
	private static final String USER_NAME = "name";

	@Override
	public void setUser(User user) throws Exception {
		ShardedJedis jedis = shardedJedisPool.getResource();

		String key = TABLE_PREFIX + user.getId();

		if (user != null) {
			jedis.hset(key, USER_ID, String.valueOf(user.getId()));
			jedis.hset(key, USER_NAME, user.getName());
		}

		shardedJedisPool.returnResource(jedis);
	}

	@Override
	public User getUser(User user) throws SQLException {
		ShardedJedis jedis = shardedJedisPool.getResource();

		String key = TABLE_PREFIX + user.getId();

		Map<String, String> retMap = jedis.hgetAll(key);

		shardedJedisPool.returnResource(jedis);

		User result = null;

		if (retMap != null) {
			String id = retMap.get(USER_ID);
			String name = retMap.get(USER_NAME);

			if (id != null && name != null) {
				User user4redis = new User();

				user4redis.setId(Integer.parseInt(id));
				user4redis.setName(name);

				result = user4redis;
			}
		}

		return result;
	}

	@Override
	public void chgUser(User user) throws Exception {
		delUser(user);
		setUser(user);
	}

	@Override
	public void delUser(User user) throws Exception {
		ShardedJedis jedis = shardedJedisPool.getResource();

		String key = TABLE_PREFIX + user.getId();

		jedis.hdel(key, USER_ID);
		jedis.hdel(key, USER_NAME);

		shardedJedisPool.returnResource(jedis);
	}
}
