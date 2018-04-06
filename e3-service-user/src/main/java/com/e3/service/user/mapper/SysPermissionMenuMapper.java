package com.e3.service.user.mapper;

import com.e3.service.user.pojo.SysPermission;

import java.util.List;

public interface SysPermissionMenuMapper {
    List<SysPermission> findByMenu(String userid);
    List<SysPermission> findByPermission(String userid);
}