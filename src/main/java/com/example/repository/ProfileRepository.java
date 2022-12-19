package com.example.repository;

import com.example.entity.ProfileEntity;
import com.example.enums.ProfileRole;
import com.example.enums.ProfileStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

public interface ProfileRepository extends CrudRepository<ProfileEntity,Integer>, PagingAndSortingRepository<ProfileEntity,Integer> {

    ProfileEntity findByPhone(String phone);

   Optional<ProfileEntity> findByPhoneAndPassword(String phone, String password);

   @Transactional
   @Modifying
   @Query("update ProfileEntity  set name=?1, surname=?2, status=?3,role=?4,visible=?5 where id=?6 ")
    int updateById(String name, String surname, ProfileStatus status, ProfileRole role,Boolean visible ,Integer id);

    @Transactional
    @Modifying
    @Query("update ProfileEntity  set name=?1, surname=?2, password=?3 where id=?4 ")
    int update(String name, String surname, String encode, Integer pId);

    @Transactional
    @Modifying
    @Query("update ProfileEntity p  set p.visible = false where p.id=?1")
    Integer deleteByIdd(Integer id);

    ProfileEntity findByEmail(String email);


}
