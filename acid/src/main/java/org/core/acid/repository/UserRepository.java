package org.core.acid.repository;

import org.core.acid.base.BaseRepository;
import org.core.acid.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends BaseRepository<User, Long> {

}