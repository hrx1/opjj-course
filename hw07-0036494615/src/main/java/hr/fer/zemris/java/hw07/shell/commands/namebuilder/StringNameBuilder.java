package hr.fer.zemris.java.hw07.shell.commands.namebuilder;

import java.util.Objects;

public class StringNameBuilder implements NameBuilder {
	private String string;
	
	public StringNameBuilder(String string) {
		Objects.requireNonNull(string);
		
		this.string = string;
	}
	
	@Override
	public void execute(NameBuilderInfo info) {
		Objects.requireNonNull(info);
		
		info.getStringBuilder().append(string);
	}

}
