package com.akka.demo.a002;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class InstructionManagerCommand implements ManagerCommand {

	private static final long serialVersionUID = 2054832663828346896L;

	@Getter
	private final String message;
}
