package ru.user.shop;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class Customer {

	private int id;
	@NonNull
	private String fullName, email;
	@NonNull
	private long phoneNumber;

}
