package com.dqy.englishstudyapi.entity.frontEntity.StudyCircleFull;

import com.dqy.englishstudyapi.tablebean.Comment;
import com.dqy.englishstudyapi.tablebean.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Repository
public class StudyCircleComment {
    Comment comment;
    ArrayList<StudyCircleSubComment> subComments;
    User user;
}
