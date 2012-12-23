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
package pandora.redis.dao;

import java.sql.SQLException;

import pandora.domain.User;

public interface RedisUserDao {
	public void setUser(User user) throws Exception;

	public User getUser(User user) throws SQLException;

	public void chgUser(User user) throws Exception;

	public void delUser(User user) throws Exception;
}
