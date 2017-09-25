package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * Created by heiqie on 2017/9/22.
 */
public interface UserRepository extends ElasticsearchRepository<User,String> {
    List<User> findByName(String name);

    List<User> deleteByName(String name);
}
