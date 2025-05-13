package com.example.systemadmin.repos;

import com.example.systemadmin.models.ServiceLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IServiceLogRepo extends JpaRepository<ServiceLog, Long> {
}
