package org.cnc.cncbot.controller;

import org.cnc.cncbot.dto.sendmessage.Message;
import org.cnc.cncbot.dto.sendmessage.MessageRequest;
import org.cnc.cncbot.dto.sendmessage.Result;
import org.cnc.cncbot.exception.BatchException;
import org.cnc.cncbot.service.WebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/cncmap")
@Slf4j
public class CNCController {

  /**
   * Game Service
   */
  private final WebService webService;

  /**
   * API Key
   * @param webService
   */
  private final String key;
  
  @Autowired
  public CNCController(WebService webService,  @Value("${cncbot.api-key}") String key) {
	  this.webService = webService;
	  this.key = key;
  }
  
  /**
   * Message service, used by SpyCNC
   * @param messageRequest
   * @return
   */
  @RequestMapping(value = "/sendMessage", method = RequestMethod.POST)
  public @ResponseBody Result messageRequest(@RequestBody MessageRequest messageRequest) {
    Result result = new Result();
    
    if(messageRequest.getKey().equals(this.key)){
      try {
      Message message = new Message();
      message.setTitre(messageRequest.getTitre());
     
      //replace = Temp fix for spycnc
      message.setMessage(messageRequest.getMessage().replace("\\n", "\n"));
      message.setPseudo(messageRequest.getJoueur());
      message.setMonde(messageRequest.getMonde());
      webService.sendMessage(message);
      
      result.setMessage("Success");
      result.setResult("OK");
      } catch (BatchException be){
        log.error("Error during message sending",be);
        result.setMessage(be.getMessage());
        result.setResult("KO");
      }  catch (Exception e){
        log.error("Error during message sending",e);
        result.setMessage("Erreur technique");
        result.setResult("KO");
      }
    } else {
      result.setMessage("Invalid KEY");
      result.setResult("KO");
    }
    
    return result;

  }
  

}