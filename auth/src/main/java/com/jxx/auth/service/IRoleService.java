package com.jxx.auth.service;

import com.jxx.auth.dto.Role;

/**
 * @author a1
 */
public interface IRoleService {
    /**
     * 添加角色可访问资源
     * @param roleId 角色ID
     * @param resourceId 资源ID
     */
    void addResource(Long roleId, Long resourceId);

    /**
     * 移除角色可访问资源
     * @param roleId 角色ID
     * @param resourceId 资源ID
     */
    void removeResource(Long roleId, Long resourceId);

    /**
     * 新建角色
     * @param name 角色名称
     * @return 角色
     */
    Role newRole(String name);

    /**
     * 检查权限
     * @param roleId 角色ID
     * @param resourceId 资源ID
     * @return 鉴权结果
     */
    boolean checkAuthority(Long roleId, Long resourceId);
}
