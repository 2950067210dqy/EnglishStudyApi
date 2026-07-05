package com.dqy.englishstudyapi.entity.frontEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Repository
public class CikuWord {
    List<String> UnMasteredWords;
    List<String>  MasteredWords;
    List<String>  VagueWords;

}
