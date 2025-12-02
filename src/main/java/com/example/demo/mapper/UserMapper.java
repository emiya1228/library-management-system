package com.example.demo.mapper;

import com.example.demo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {

    /**
     * 根据ID查询用户
     */
    User selectById(Integer id);

    /**
     * 根据用户名查询用户
     */
    User selectByUsername(String username);

    /**
     * 根据邮箱查询用户
     */
    User selectByEmail(String email);

    /**
     * 查询所有用户
     */
    List<User> selectAll();

    /**
     * 根据条件查询用户
     */
    List<User> selectByCondition(Map<String, Object> condition);

    /**
     * 插入用户
     */
    int insert(User user);

    /**
     * 更新用户
     */
    int update(User user);

    /**
     * 根据ID删除用户
     */
    int deleteById(Long id);

    /**
     * 根据用户名和密码查询用户（用于登录）
     */
    User selectByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    /**
     * 根据角色查询用户
     */
    List<User> selectByRole(String role);

    /**
     * 根据状态查询用户
     */
    List<User> selectByStatus(String status);

    /**
     * 更新用户登录状态
     */
    int updateLoginStatus(@Param("id") Long id, @Param("loginStatus") String loginStatus);

    /**
     * 更新最后登录时间
     */
    int updateLastLoginTime(Long id);

    /**
     * 检查用户名是否存在
     */
    boolean existsByUsername(String username);

    /**
     * 检查邮箱是否存在
     */
    boolean existsByEmail(String email);

    /**
     * 分页查询用户
     */
    List<User> selectByPage(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 统计用户数量
     */
    Long count();
}