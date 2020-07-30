package com.akka.demo.a002;

import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class ResultManagerCommand implements ManagerCommand{

	private static final long serialVersionUID = -987952783096099199L;

	@Getter
	private final BigInteger prime;
}
