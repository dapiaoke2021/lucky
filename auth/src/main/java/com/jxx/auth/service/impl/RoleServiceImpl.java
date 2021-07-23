package com.jxx.auth.service.impl;

import com.jxx.auth.dto.Role;
import com.jxx.auth.service.IRoleService;
import org.springframework.stereotype.Service;

/**
 * @author a1
 */
@Service
public class RoleServiceImpl implements IRoleService {
    @Override
    public void addResource(Long roleId, Long resourceId) {

    }

    @Override
    public void removeResource(Long roleId, Long resourceId) {

    }

    @Override
    public Role newRole(String name) {
        return null;
    }

    @Override
    public boolean checkAuthority(Long roleId, Long resourceId) {
        return false;
    }
}
