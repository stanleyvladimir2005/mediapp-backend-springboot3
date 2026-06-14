package com.mitocode.serviceimpl;

import com.mitocode.model.Menu;
import com.mitocode.repo.IGenericRepo;
import com.mitocode.repo.IMenuRepo;
import com.mitocode.service.IMenuService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl extends CRUDImpl<Menu, Integer> implements IMenuService {

    private final IMenuRepo repo;

    @Override
    public List<Menu> getMenusByUsername(String username) {
        //Para obtener el usuario de la session de spring security
        val contextSessionUser = SecurityContextHolder.getContext().getAuthentication().getName();
        return repo.getMenusByUsername(contextSessionUser);
    }

    @Override
    protected IGenericRepo<Menu, Integer> getRepo() {
        return repo;
    }
}