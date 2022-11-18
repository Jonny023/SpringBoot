package org.core.acid.system.repository;

import org.core.acid.base.BaseRepository;
import org.core.acid.system.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends BaseRepository<User, Long> {

}