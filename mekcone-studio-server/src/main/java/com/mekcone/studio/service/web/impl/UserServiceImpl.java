package com.mekcone.studio.service.web.impl;

import com.mekcone.studio.entity.DTO.User;
import com.mekcone.studio.repository.UserRepository;
import com.mekcone.studio.service.web.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 * Author: Tenton Lien
 * Date: 9/20/2020
 */

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean login(HttpServletResponse httpServletResponse, User user) {
        return false;
    }

    @Override
    public boolean logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return false;
    }

    @Override
    public boolean getLoginStatus(HttpServletRequest httpServletRequest) {
        return false;
    }

    @Override
    public void createAccount(User user) {

    }

    @Override
    public User getAccount(String accountId) {
        return null;
    }

    @Override
    public void updateAccount() {

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("用户名不对");
        }

//        List<TtRole> roles = ttRoleMapper.getUserRole(user.getUserId());

        //由于mybatis-plus会将TtUser对象中的属性封装到sql中进行查询，所以没有直接在TtUser对象中增加Role属性，而是通过TransformUtils中的beanToBean方法，将TtUser转换成实现UserDetail方法的UserVO对象。beanToBean方法会将相同的属性进行值传递，不同属性则不会变化。
//        UserVO userVO = TransformUtils.beanToBean(user, UserVO.class);
//        userVO.setRoles(roles);

        return user;
    }
}
