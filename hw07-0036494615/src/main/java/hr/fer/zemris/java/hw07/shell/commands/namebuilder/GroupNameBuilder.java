package hr.fer.zemris.java.hw07.shell.commands.namebuilder;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GroupNameBuilder implements NameBuilder {
	private int index;
	private String delimiter;
	private int minLength;
	
	public GroupNameBuilder(int index) {
		this(index, '0', 0);
	}
	
	public GroupNameBuilder(int index, char delimiter, int minLength) {
		this.index = index;
		this.delimiter = String.valueOf(delimiter);
		this.minLength = minLength;
	}
	
	@Override
	public void execute(NameBuilderInfo info) {
		StringBuilder result = info.getStringBuilder();
		String group = info.getGroup(index).toString();
		
		if(group.length() < minLength) {
			String sRepeated = IntStream.range(0, minLength - group.length()).mapToObj(i -> delimiter).collect(Collectors.joining(""));
			result.append(sRepeated);
		}
		
		result.append(group);
	}

}
