package com.mitocode.serviceimpl;

import com.mitocode.model.Sing;
import com.mitocode.repo.IGenericRepo;
import com.mitocode.repo.ISingRepo;
import com.mitocode.service.ISingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SingServiceImpl extends CRUDImpl<Sing, Integer> implements ISingService {

    private final ISingRepo repo;

    @Override
    protected IGenericRepo<Sing, Integer> getRepo() {
        return repo;
    }
}