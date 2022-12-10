package cn.youngkbt.generic;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

/**
 * @author Kele-Bingtang
 * @date 2022/12/10 18:17
 * @note
 */
@SpringBootTest
public class RedisStringTest {
    
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    
    @Test
    public void set() {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set("test", "测试");
    }
}
