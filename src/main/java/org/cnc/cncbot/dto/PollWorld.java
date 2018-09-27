package org.cnc.cncbot.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PollWorld {

private String Type;

private List<S> s = new ArrayList<S>();

private List<String> ch = new ArrayList<String>();

}