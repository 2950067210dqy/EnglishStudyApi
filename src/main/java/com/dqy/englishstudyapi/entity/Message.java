package com.dqy.englishstudyapi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Repository;

@Data
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Repository
public class Message {
    String textMessage;
    String userImgSrc;
    String dataTIme;
    String msg_type;
    Integer type;
    String voiceSrc;//录音的路径
    Integer voiceTime;//录音的时长
    String sendImgSrc;//图片路径
}
