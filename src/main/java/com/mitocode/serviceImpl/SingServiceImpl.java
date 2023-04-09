package com.mitocode.serviceImpl;

import com.mitocode.model.Sing;
import com.mitocode.repo.IGenericRepo;
import com.mitocode.repo.ISingRepo;
import com.mitocode.service.ISingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SingServiceImpl extends CRUDImpl<Sing, Integer> implements ISingService {

    @Autowired
    private ISingRepo repo;

    @Override
    protected IGenericRepo<Sing, Integer> getRepo() {
        return repo;
    }
}