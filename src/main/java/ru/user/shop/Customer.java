package ru.user.shop;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class Customer {

	private final int id;
	private String fullName, email;
	private long phoneNumber;

}
