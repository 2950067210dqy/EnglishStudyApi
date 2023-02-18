package com.dqy.englishstudyapi.entity.frontEntity.CikuFull;
import com.dqy.englishstudyapi.tablebean.Ciku;
import com.dqy.englishstudyapi.tablebean.Cikutype;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Repository;
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Repository
public class CikuFull{
    Ciku ciku;
    Integer count;
    Integer newCikuTypeId;
}