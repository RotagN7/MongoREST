package org.test;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.web.context.annotation.RequestScope;

@RequestScope
public interface AccountRepository extends MongoRepository<Account, String>{
}
