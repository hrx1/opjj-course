package hr.fer.zemris.java.hw07.shell.commands.namebuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class NameBuilderInfo {
	private List<String> groups;
	private StringBuilder stringBuilder = new StringBuilder();
	
	public String getGroup(int index) {
		return groups.get(index);
		
	}
	
	public StringBuilder getStringBuilder() {
		return stringBuilder;
	}
	
	public static NameBuilderInfo fromMatcher(Matcher matcher) {
		
		NameBuilderInfo nb = new NameBuilderInfo();
		if(!matcher.find()) return nb;

		int groupCount = matcher.groupCount() + 1; //zbog toga sto matcher pod 0 stavlja cijeli string, a grupe broji od 1
		nb.groups = new ArrayList<>(groupCount);
		
		for(int i = 0; i < groupCount; ++i) { 
			nb.groups.add(matcher.group(i));
		}
		
		return nb;
	}
	
}
