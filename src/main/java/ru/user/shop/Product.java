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
public class Product {
	private int id;
	@NonNull
	private String name;
	@NonNull
	private int article, amount, supplyId;
	@NonNull
	private double price;

	protected Product() {
	}
}
