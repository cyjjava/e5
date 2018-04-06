package com.e3.service.user.api;

import com.e3.E3Result;
import com.e3.service.user.pojo.TbUser;

/**
 * Created by CYJ on 2018/3/6.
 */
public interface UserService {
   //dasd
    E3Result authRityUsername(String username,String pwd);

    E3Result checkUserParam(String params, int type);

    E3Result registerUser(TbUser tbUser);

    E3Result loginUser(String username, String password);

    E3Result getBytoken(String token);

    E3Result delToken(String token);

    E3Result getBytokenUser(String token);
}
