package org.cnc.cncbot.dto.sendmessage;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageRequest {

  String key;
  String titre;
  String message;
  String joueur;
  int monde;

}