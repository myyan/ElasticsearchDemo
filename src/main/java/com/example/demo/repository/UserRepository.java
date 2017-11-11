package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 *
 * @author heiqie
 * @date 2017/9/22
 */
public interface UserRepository extends ElasticsearchRepository<User,String> {
    /**
     * 默认只返回10条
     * @param name
     * @return
     */
    List<User> findByName(String name);


    List<User> deleteByName(String name);
}
