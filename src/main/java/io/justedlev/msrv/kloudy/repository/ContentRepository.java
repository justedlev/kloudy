package io.justedlev.msrv.kloudy.repository;

import org.springframework.core.io.Resource;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.InputStream;

@NoRepositoryBean
public interface ContentRepository<I> {
    long set(I sid, InputStream io);

    Resource get(I sid);

    boolean delete(I sid);
}
