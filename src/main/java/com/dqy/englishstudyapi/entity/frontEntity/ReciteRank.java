package com.dqy.englishstudyapi.entity.frontEntity;

import com.dqy.englishstudyapi.tablebean.Recitedatasum;
import com.dqy.englishstudyapi.tablebean.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServer;
import org.springframework.stereotype.Repository;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Repository
public class ReciteRank {
   User user;
   Recitedatasum recitedatasum;
}
