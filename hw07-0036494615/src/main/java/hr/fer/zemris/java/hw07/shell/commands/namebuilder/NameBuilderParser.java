package hr.fer.zemris.java.hw07.shell.commands.namebuilder;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NameBuilderParser {
	EndNameBuilder end;
	
	public NameBuilderParser(String expression) {
		List<NameBuilder> nameBuilders = new LinkedList<>();
		
		String patternString = "(\\$\\{.+?\\})|([^${}]+)";
		
		Pattern paranthesis = Pattern.compile(patternString);
		
		Matcher matcher = paranthesis.matcher(expression);
		
		while(matcher.find()) {
			String found = matcher.group();
			
			if(found.startsWith("${")) {
				
				String[] tmp = found.replace("${", "").replace("}", "").split(",");
				
				int index = Integer.valueOf(tmp[0]);
				
				char delimiter;
				int minLength;
				if(tmp.length == 1) {
					delimiter = ' ';
					minLength = 0;
				}
				else if(tmp[1].startsWith("0")) {
					delimiter = '0';
					minLength = Integer.valueOf(tmp[1]);
				}
				else {
					delimiter = ' ';
					minLength = Integer.valueOf(tmp[1]);
				}
				
				
				
				nameBuilders.add(new GroupNameBuilder(index, delimiter, minLength));
			}else {
				nameBuilders.add(new StringNameBuilder(found));
			}
			
		}
		
		
		end = new EndNameBuilder(nameBuilders);
	}
	
	public NameBuilder getNameBuilder() {
		return end;
	}

}
