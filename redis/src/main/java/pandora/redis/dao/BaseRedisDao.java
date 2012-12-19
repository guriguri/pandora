package pandora.redis.dao;

import redis.clients.jedis.ShardedJedisPool;

public abstract class BaseRedisDao {
	protected ShardedJedisPool shardedJedisPool;

	public void setShardedJedisPool(ShardedJedisPool shardedJedisPool) {
		this.shardedJedisPool = shardedJedisPool;
	}
}
