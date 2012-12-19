package pandora.redis.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

public class RedisPoolFactory implements FactoryBean<ShardedJedisPool>,
		InitializingBean {
	private static final String SEPARATOR = ",";

	private ShardedJedisPool shardedJedisPool;
	private String shards;
	private JedisPoolConfig poolConfig;

	private int timeout;

	public void setPoolConfig(JedisPoolConfig poolConfig) {
		this.poolConfig = poolConfig;
	}

	public void setShards(String shards) {
		this.shards = shards;
	}

	protected List<String> getShardList() {
		List<String> shardList = new ArrayList<String>();
		String[] tmp = shards.split(SEPARATOR);

		for (int i = 0, size = tmp.length; i < size; i++) {
			shardList.add(tmp[i].trim());
		}

		return shardList;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	@Override
	public ShardedJedisPool getObject() throws Exception {
		return shardedJedisPool;
	}

	@Override
	public Class<?> getObjectType() {
		return shardedJedisPool.getClass();
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public List<JedisShardInfo> buildShardInfo(List<String> shards, int timeout) {
		List<JedisShardInfo> shardsList = new ArrayList<JedisShardInfo>();

		for (String shard : shards) {
			System.out.println("INFO, shard: [" + shard + "]");

			String[] token = shard.split(":");
			String host = token[0];
			int port = Integer.valueOf(token[1]);

			JedisShardInfo si = new JedisShardInfo(host, port, timeout);
			shardsList.add(si);
		}

		return shardsList;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		List<JedisShardInfo> shardsList = buildShardInfo(getShardList(),
				timeout);

		shardedJedisPool = new ShardedJedisPool(poolConfig, shardsList);
	}
}
