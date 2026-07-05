package com.dqy.englishstudyapi.entity.adminEntity.condition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Repository;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Repository
public class UserCondition extends BaseCondition{
   String usernameSearch;
   String passwordSearch;
   String  nameSearch;

   String phoneSearch;
   String emailSearch;

   List<Integer> sexSelect;
   List<String> birthdayDate;


}
