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
public class Warehouse {

	private int id;
	@NonNull
	private String address;
	@NonNull
	private int shopId;

	protected Warehouse() {
	}
}
