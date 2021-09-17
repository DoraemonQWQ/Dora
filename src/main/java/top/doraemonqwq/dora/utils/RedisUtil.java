package top.doraemonqwq.dora.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Doraemon
 * @date 2021-09-13
 * redis工具类
 */
@Component
public class RedisUtil {
    @Autowired
    private RedisTemplate redisTemplate;
    public static final String LEFT = "left";
    public static final String RIGHT = "right";

    /**
     * 指定缓存失效时间
     * @param key 键
     * @param time 时间（单位/秒)
     * @return true为设置时间成功，false为设置时间失败
     */
    public boolean exprie (String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 通过key值，获得当前key的过期时间
     * @param key 键
     * @return 过期时间(秒) 若返回0就代表永久有效
     */
    public long getExpire (String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     * @param key 键
     * @return true存在 false不存在
     */
    public boolean hasKey (String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除缓存
     * @param key 键 可以传一个key或多个key
     */
    public void del (String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    /**
     * 获取基本类型的缓存
     * @param key 对应缓存的键
     * @return 有缓存就返回缓存本身，没有就返回null
     */
    public Object get (String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 存储基本类型的缓存
     * @param key 键
     * @param value 值
     * @return true为存储成功，false为存储失败
     */
    public boolean set (String key, Object value) {
        return this.set(key, value, 0);
    }

    /**
     * 存储基本类型的缓存并设置有效时间
     * @param key 键
     * @param value 值
     * @param time 有效时间(秒)
     * @return true为成功，false为失败
     */
    public boolean set (String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                redisTemplate.opsForValue().set(key,value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 递增
     * @param key 键
     * @param delta 递增因子(要增加的次数。必须大于0)
     * @return
     */
    public long incr (String key, long delta) {
        if (delta > 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    public long decr (String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须小于0");
        }
        return redisTemplate.opsForValue().increment(key , -delta);
    }


    /**
     * 获取hashKey对应的所有键值对
     * @param key 键
     * @return 对应多个键值对
     */
    public Map<String,Object> hashGet (String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 存储map中的键值对到hash表中,并设置有效时间
     * @param key 键
     * @param map 键值对
     * @param time 有效时间(秒)
     * @return true为成功，false为失败
     */
    public boolean hashSet (String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                exprie(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 存储map中的键值对到hash表中
     * @param key 键
     * @param map 键值对
     * @return true为成功，false为失败
     */
    public boolean hashSet (String key, Map<String, Object> map) {
        return this.hashSet(key, map, 0);
    }


    /**
     * 向指定hash表中的存储一个键值对，如果指定hash表不存在，那么就新建一个，并设置时间（若原先的hash表有时间，那么将会覆盖）
     * @param hashKey hash表的键
     * @param key 要存储的键
     * @param value 要存储的值
     * @param time 时间(秒)
     * @return true为成功，false为失败
     */
    public boolean hashSetByValue (String hashKey, String key, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(hashKey, key, value);
            if (time > 0) {
                exprie(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向指定hash表中存储一个键值对，如果指定hash表不存在，那么就新建一个
     * @param hashKey hash表的键
     * @param key 要存储的键
     * @param value 要存储的值
     * @return true为成功，false为失败
     */
    public boolean hashSetByValue (String hashKey, String key, Object value) {
        return this.hashSetByValue(hashKey, key, value, 0);
    }


    /**
     * 删除hash表中的键值对
     * @param hashKey hash表的key值
     * @param key hash表中键值对的key值，可以是一个也可以是多个，但不能为空
     */
    public void hashDelete (String hashKey, String... key) {
        redisTemplate.opsForHash().delete(hashKey,key);
    }

    /**
     * 判断指定hash表中是否存在指定key的键值对
     * @param hashKey hash表的key值
     * @param key 需要查询的key值
     * @return true为存在，false为不存在
     */
    public boolean hashHasKey (String hashKey, String key) {
        return redisTemplate.opsForHash().hasKey(hashKey, key);
    }

    /**
     * hash表递增，如果递增的key不存在，就新建一个，并把新建后的value返回
     * @param hashKey hash的key值
     * @param key 要递增的key值
     * @param delta 要递增的次数(大于0)
     * @return 返回值为0时，代表delta值为0
     */
    public double hashIncr (String hashKey, String key, double delta) {
        if (delta > 0) {
            return redisTemplate.opsForHash().increment(hashKey, key, delta);
        } else if (delta < 0) {
            return redisTemplate.opsForHash().increment(hashKey, key, -delta);
        }else if (delta == 0) {
            throw new RuntimeException("递增/递减的因子不能为0");
        }
        return 0;
    }

    /**
     * hash表递减
     * @param hashKey hash类型的key值
     * @param key 要递减的key值
     * @param delta 要递减的次数(小于0)
     * @return
     */
    public double hashDecr (String hashKey, String key, double delta) {
        return this.hashIncr(hashKey, key, delta);
    }


    /**
     * 根据Set类型的key得到value
     * @param key 键
     * @return 返回value为成功，返回null为失败
     */
    public Set<Object> setGet (String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据value值从一个set数据中查找，是否存在
     * @param key set的key值
     * @param value 需要查找的value值
     * @return 如果有就返回true，没有就返回false
     */
    public boolean setHasKey (String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将数据添加到set类型的key中
     * @param key set类型的key值
     * @param value 需要存储的value数据
     * @return 返回成功的个数
     */
    public long sSet (String key, Object... value) {
        try {
            return redisTemplate.opsForSet().add(key,value);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 将数据添加到set类型的key中，并设置有效时间
     * @param key set类型的key值
     * @param time 有效时间
     * @param value 需要存储的value数据
     * @return 返回成功的个数
     */
    public long sSetTime (String key, long time, Object... value) {
        try {
            long count = this.sSet(key, value);
            if (time > 0) {
                exprie(key, time);
            }
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 查询Set类型的key的长度
     * @param key Set类型的key
     * @return 返回长度
     */
    public long setGetSetSize (String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 将Set中的value删除
     * @param key Set的key
     * @param value 需要删除的value 可以是多个
     * @return 返回删除成功的个数
     */
    public long setRemove (String key, Object... value) {
        try {
            return redisTemplate.opsForSet().remove(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 查询list中的缓存个数
     * @param key   list的key
     * @param start 开始位置
     * @param end   结束位置 0到-1代表所有
     * @return
     */
    public List<Object> listGet (String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 获取list的长度
     * @param key   list的key值
     * @return      返回长度
     */
    public long listSetSize (String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 通过下标索引获取list类型的缓存中的value值
     * @param key   list的key值
     * @param index list的下标索引
     * @return 返回value自身，没有就返回null
     */
    public Object listGetIndex (String key, int index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将数据存入list缓存中 指定添加位置(left/right) 默认为right
     * @param key       list的key值
     * @param location  添加的位置(left/right) 默认为right
     * @param value     需要添加的数据 可以是多个
     * @return          返回添加成功的个数
     */
    public long listSet (String key, String location, Object... value) {
        try {
            if (location.equals("left")) {
                return redisTemplate.opsForList().leftPushAll(key, value);
            } else {
                return redisTemplate.opsForList().rightPushAll(key, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 修改list缓存中指定下标索引的value数据
     * @param key   list的key值
     * @param index 需要修改的下标索引
     * @param value 新的value值
     * @return true为成功，false为失败
     */
    public boolean listUpdateIndex (String key, int index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 从list缓存中移除N个相同的value值
     * @param key   list的key值
     * @param count 移除的个数
     * @param value 移除的目标
     * @return      移除数量
     */
    public long listRemove (String key, int count, Object value) {
        try {
            return redisTemplate.opsForList().remove(key, count, value);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


}
