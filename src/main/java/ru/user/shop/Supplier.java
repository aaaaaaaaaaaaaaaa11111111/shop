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
public class Supplier {
	private int id;
	@NonNull
	private String name, email;
	@NonNull
	private long phoneNumber;
	@NonNull
	private String address;
	@NonNull
	private long inn;
	@NonNull
	private int rating;

	protected Supplier() {
	}
}
