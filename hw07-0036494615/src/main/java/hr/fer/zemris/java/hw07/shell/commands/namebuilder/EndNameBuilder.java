package hr.fer.zemris.java.hw07.shell.commands.namebuilder;

import java.util.List;

public class EndNameBuilder implements NameBuilder {
	List<NameBuilder> nameBuilders;
	
	public EndNameBuilder(List<NameBuilder> nameBuilders) {
		this.nameBuilders = nameBuilders;
	}
	
	@Override
	public void execute(NameBuilderInfo info) {
		for(NameBuilder nb : nameBuilders) {
			nb.execute(info);
		}
	}

	
}
