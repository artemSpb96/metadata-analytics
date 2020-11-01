package ru.spbu.metadata.api.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.spbu.metadata.api.domain.Filesystem;

import java.time.LocalDateTime;

@Repository
public interface FilesystemRepository extends CrudRepository<Filesystem, Integer> {
    @Modifying
    @Query("UPDATE filesystem SET active_ver = :activeVer, update_time = :updateTime WHERE fs_id = :id")
    boolean updateActiveVersion(
            @Param("id") int id,
            @Param("activeVer") int activeVersion,
            @Param("updateTime")LocalDateTime updateTime
    );
}
